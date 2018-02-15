package data.db;

import java.io.Closeable;
import java.io.File;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.qbao.search.conf.LoadValues;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import util.PageConfig;
import vo.CommandPo;

public class IndexUpdate implements Closeable {

	private static ESLogger logger = Loggers.getLogger(IndexUpdate.class);
	private static IndexUpdate indexUpdate;
	private static String user = Config.getBase().get(LoadValues.DATA_USERNAME).trim();
	private static String pwd = Config.getBase().get(LoadValues.DATA_PASSWORD).trim();
	private static String url = Config.getBase().get(LoadValues.DATA_CONNECTION).trim();
	/** conn链接过期时间 **/
	private int CONNECT_Time_OUT = Config.get().getInt("es.connect.timeout.second", 1800);
	private int pageSize = Config.get().getInt("es.index.import.pagesize", 50000);
	/** 本机备份文件路径 **/
	private static String backupFilePath = System.getProperty("user.dir") + File.separator + "ms_es_file_bk_";
	private static Connection conn;
	private static TransportClient client;

	/** 分页容器 **/
	private PageConfig<Object> pageConfig;

	// 任务记录log
	private static StringBuffer taskLog = new StringBuffer();

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	public static final IndexUpdate getInstance() {
		try {
			if (indexUpdate == null) {
				synchronized (IndexUpdate.class) {
					indexUpdate = new IndexUpdate();
				}
			}
		} catch (Exception ex) {
			logger.info(new Date().toLocaleString() + "//fail to index :405");
			ex.printStackTrace();
		}
		return indexUpdate;
	}

	private static void GetEsClient() {
		// -----------------es集群连接--------------------
		Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
				.put("client.transport.ignore_cluster_name", true)// 忽略集群名字验证,打开后集群名字不对也能连接上
				// .put("client.transport.sniff", true) // 自动侦听新增节点
				.build();

		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(
						new InetSocketAddress(Config.get().get("es.host1"), Config.get().getInt("es.port", 9300))))
				.addTransportAddress(new InetSocketTransportAddress(
						new InetSocketAddress(Config.get().get("es.host2"), Config.get().getInt("es.port", 9300))))
				.addTransportAddress(new InetSocketTransportAddress(
						new InetSocketAddress(Config.get().get("es.host3"), Config.get().getInt("es.port", 9300))));
	}

	/** 根据日期过滤总结果数目得到分页总数 **/
	private boolean getPageConfig(String table,String date,String dt) {
		PreparedStatement st;
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			String dateConst = "";
			if(date!=null&&!"".equals(date)){
				dateConst = " and "+dt + "='"+date+"'";
			}
			st = conn.prepareStatement("select count(*) from "+table+" where  score > 0 "+dateConst);
			st.setQueryTimeout(CONNECT_Time_OUT);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				taskLog.append(new Date().toLocaleString() + "---------->select count(*) from "+table+" success, results=="
						+ rs.getInt(1) + "<br/>");
				logger.info(new Date().toLocaleString() + "---------->select count(*) from "+table+" success, results=="
						+ rs.getInt(1));
				if (rs.getInt(1) > 0) {
					pageConfig = new PageConfig<Object>(rs.getInt(1), pageSize, 1);
					return true;
				} else {
					logger.info(new Date().toLocaleString() + "//fail to index :406-1");
					return false;
				}

			}

		} catch (SQLException e) {
			logger.info(new Date().toLocaleString() + "//fail to index :406-2");
			e.printStackTrace();
		} finally {
			closeJdbcconnect();
		}
		logger.info(new Date().toLocaleString() + "//fail to index :406-3");
		return false;

	}

	/**
	 * 停止正在做索引
	 **/
	public String StopTask() {
		try {
			IndexUpdate.getInstance().close();
			return IndexUpdate.getInstance().taskLog.append("index task stop <br/>").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return IndexUpdate.getInstance().taskLog.toString();
	}

	/**
	 * 开始分页做索引
	 **/
	public synchronized String DoTask(CommandPo po, boolean fileBackup) {

		try {
			String date = po.getDate();
			String table = po.getTable();
			String index = po.getIndex();
			String key = po.getKey();
			String dt = po.getDt();
			if(StringUtils.isEmpty(index)||StringUtils.isEmpty(index)||StringUtils.isEmpty(index)||StringUtils.isEmpty(dt)){
				IndexUpdate.getInstance().taskLog.append("parameter table,index,key,dt is not null 412!<br/>").toString();
			}
			if(StringUtils.isEmpty(date)){
				date = sdf.format(new Date());
			}
			IndexUpdate.getInstance().taskLog = new StringBuffer();
			if (IndexUpdate.getInstance().getPageConfig(table,date,dt)) {

				for (int i = 1; i <= IndexUpdate.getInstance().pageConfig.getTotalPageNum(); i++) {

					IndexUpdate.getInstance().IndexBathImport(index,
							index, date,
							(i - 1) * IndexUpdate.getInstance().pageSize, IndexUpdate.getInstance().pageSize,
							fileBackup,table,key,dt);

					IndexUpdate.getInstance().pageConfig.setCurrentPage(i + 1);

				}
				IndexUpdate.getInstance().taskLog.append(new Date() + "------>update "+index+" index success !<br/>");
				logger.info(new Date() + "------>update "+index+" index success !");
			} else {
				IndexUpdate.getInstance().taskLog.append(new Date().toLocaleString() + "<br/>");
				IndexUpdate.getInstance().taskLog.append("fail to index :401\n");
				return IndexUpdate.getInstance().taskLog.toString();
			}

			return IndexUpdate.getInstance().taskLog.toString();

		} catch (Exception e) {
			e.printStackTrace();
			client.close();
		}

		return IndexUpdate.getInstance().taskLog.append("unkown error 411!<br/>").toString();
	}

	/**
	 * 从数据库批量导入数据
	 * 
	 * @throws Exception
	 */
	private void IndexBathImport(String indexName, String type, String date, long startLine, long lastLine,
			boolean fileBackup,String table,String keys,String dt) {
		PreparedStatement st;
		GetEsClient();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		/** 本地备份用 **/
		ArrayList<String> indexTextLines = new ArrayList<String>();
		String indexTxtFileName = date + ".txt";
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			String dateConst = "";
			if(date!=null&&!"".equals(date)){
				dateConst = " and "+dt + "='"+date+"'";
			}
			st = conn.prepareStatement(
					"select * from "+table+" where  score > 0 "+dateConst + " limit " + startLine + "," + lastLine);

			st.setQueryTimeout(CONNECT_Time_OUT);

			ResultSet rs = st.executeQuery();
			logger.info(new Date() + "------>select from "+table+" success , page=" + this.pageConfig.getCurrentPage()
					+ ",pageSize=" + this.pageSize);
			taskLog.append(new Date() + "------>select from "+table+" success , page=" + this.pageConfig.getCurrentPage()
					+ ",pageSize=" + this.pageSize + "<br/>");
			Map<String, Object> ret = new LinkedHashMap<String, Object>();
			String columName;
			String[] keyArray = keys.split(",");
			String key = keys;
			Boolean isMultyFields = false;
			int isMultyFieldsSize = keyArray.length;
			if(keyArray.length>1){
				key = "";
				isMultyFields = true;
			}
			int count = 0;
			while (rs.next()) {
				StringBuffer indexPoStr = new StringBuffer();

				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					columName = rs.getMetaData().getColumnName(i);
					switch (rs.getMetaData().getColumnTypeName(i).trim()) {
					case "CHAR":
						

						ret.put(columName, rs.getString(columName));

						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;

					case "VARCHAR":
						
						ret.put(columName, rs.getString(columName));

						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;

					case "SMALLINT":
						ret.put(columName, rs.getInt(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;

					case "INT":
						ret.put(columName, rs.getInt(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;

					case "BIGINT":
						ret.put(columName, rs.getInt(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;

					case "LONG":
						ret.put(columName, rs.getLong(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;
					case "DATE":
						ret.put(columName, rs.getDate(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;
					case "DECIMAL":
						ret.put(columName, rs.getBigDecimal(columName));
						break;
					case "FLOAT":
						ret.put(columName, rs.getFloat(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;
					case "DATETIME":
						ret.put(columName, rs.getTimestamp(columName));
						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
					default:
						/**
						 * java.lang.Boolean \java.lang.Byte \java.lang.Short
						 * java.lang.Double\ java.sql.Time \java.sql.Timestamp
						 * java.sql.Blob\ java.sql.Clob对没有加入的类型一律用sting 做索引
						 **/
						

						ret.put(columName, rs.getString(columName));

						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;
					}

				}
				if(isMultyFields){
					key = "";
					for(int i=0;i<isMultyFieldsSize;i++){
						key = key + rs.getString(keyArray[i])+"_";
					}
					key = key.substring(0,key.length()-1);
				}else{
					key  = rs.getString(keys);
				}
				bulkRequest.add(client.prepareIndex(indexName, type).setId(key).setSource(ret));
				indexTextLines.add(indexPoStr.toString());
				count++;
			}
			bulkRequest.execute().actionGet();
			logger.info(new Date() + "------> "+indexName+" index batch input page=" + this.pageConfig.getCurrentPage()
					+ ",records=" + count);
			taskLog.append(new Date() + "------> "+indexName+" index batch input page=" + this.pageConfig.getCurrentPage()
					+ ",records=" + count + "<br/>");

			if (fileBackup) {

				util.FileUtil.inputTXT(backupFilePath, indexTxtFileName, indexTextLines, true);
				logger.info(new Date() + "------> "+indexName+" index backup File,path=" + backupFilePath + indexTxtFileName
						+ ",page=" + this.pageConfig.getCurrentPage() + ",records=" + indexTextLines.size() + "<br/>");
				taskLog.append(new Date() + "------> "+indexName+" index backup File,path=" + backupFilePath + indexTxtFileName
						+ ",page=" + this.pageConfig.getCurrentPage() + ",records=" + indexTextLines.size() + "<br/>");
			}
		} catch (SQLException e) {
			logger.info(new Date().toLocaleString() + "//fail to index :409");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeJdbcconnect();
			client.close();
		}

	}

	private void indexPoWrap(StringBuffer indicPo, String columType, String columName, String columValue) {

		indicPo.append(columType).append("]~[").append(columName).append("]~[").append(columValue).append("<=>");

	}

	@Override
	public void close() {
		if (conn != null) {
			try {
				conn.close();
				client.close();
			} catch (SQLException e) {
				logger.info(new Date().toLocaleString() + "//fail to index :407");
				e.printStackTrace();
			}

		}

	}

	private void closeJdbcconnect() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.info(new Date().toLocaleString() + "//fail to index :407");
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws Exception {
		CommandPo po = new CommandPo();
		po.setDate("2017-09-12");
		po.setIndex("sfnewstype");
		po.setTable("t_result_oneday_news_type_d");
		po.setKey("user_id,news_type");
		po.setDt("stat_date");
		//导入新闻类别
		//IndexUpdate.getInstance().DoTask(po, false);

		//导入模块
//		po.setIndex("sfmodule");
//		po.setTable("t_result_oneday_module_d");
//		po.setKey("user_id,hour,module_id");
//		IndexUpdate.getInstance().DoTask(po, false);
//
//		//导入时间轴
//		po.setDate("2017-09-13");
//		po.setIndex("sftimeaxis");
//		po.setTable("t_result_oneday_timeaxis_d");
//		po.setKey("user_id,hour");


		po.setDate("2017-09-18");
		po.setIndex("sftimeall");
		po.setTable("t_result_oneday_all_d");
		po.setKey("user_id,cluster_id,module_id");
		IndexUpdate.getInstance().DoTask(po, false);
		// logger.info(IndexUpdate.getInstance().taskLog);

		System.exit(0);
	}

}

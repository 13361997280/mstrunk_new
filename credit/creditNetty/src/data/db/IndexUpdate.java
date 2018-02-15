package data.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import util.DateUtil;
import util.PageConfig;
import vo.CommandPo;

import java.io.Closeable;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class IndexUpdate implements Closeable {

	private static ESLogger logger = Loggers.getLogger(IndexUpdate.class);
	private static IndexUpdate indexUpdate;
	private static String user = Config.getBase().get(LoadValues.CREDIT_USERNAME).trim();
	private static String pwd = Config.getBase().get(LoadValues.CREDIT_PASSWORD).trim();
	private static String url = Config.getBase().get(LoadValues.CREDIT_CONNECTION).trim();
	/** conn链接过期时间 **/
	private int CONNECT_Time_OUT = Config.get().getInt("es.connect.timeout.second", 1800);
	private int pageSize = Config.get().getInt("es.index.import.pagesize", 5000);
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
				dateConst = dt + "='"+date+"'";
			}
			st = conn.prepareStatement("select count(*) from "+table+" where status in (0,1) and "+dateConst);
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
				dateConst = dt + "='"+date+"'";
			}
			st = conn.prepareStatement(
					"select adjust_total_score credit_score,user_id from " + table + " where status in (0,1) and " + dateConst + " limit " + startLine + "," + lastLine);

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
				Long start = System.currentTimeMillis();
				StringBuffer indexPoStr = new StringBuffer();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					columName = rs.getMetaData().getColumnLabel(i);
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

				GetRequestBuilder requestBuilder = client.prepareGet(indexName,type,ret.get("user_id").toString())
						.setFetchSource(new String[]{"total_score","credit_score"},null);
				Map sources = requestBuilder.execute().actionGet().getSourceAsMap();

				//设置更新时间
				ret.put("update_time", DateUtil.dateToDateString(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));

				//如果es还没有数据。则将信用分赋值总分更新到es
				if (sources == null){
					ret.put("total_score", ret.get("credit_score"));

					// 新增数据
					bulkRequest.add(client.prepareIndex(indexName, type).setId(key).setSource(ret));
				}

				//ES有数据记录。进行数据计算更新信用分和总分
				//    !(((float)ret.get("credit_score")) == ((float)sources.get("credit_score")))  数据库和es信用分不相等。说明数据库更新了信用分
				if (sources != null && !ret.get("credit_score").equals(sources.get("credit_score"))) {

					Integer esCredit = sources.get("credit_score") == null ? 0 : ((Double) sources.get("credit_score")).intValue();        //es信用分

					Integer dbCredit = (Integer) ret.get("credit_score");		//数据库信用分

					Integer diffCredit = dbCredit - esCredit;					//差值
																				//ES信用总分
					float totalCredit;
					if (sources.get("total_score") == null){
						totalCredit = dbCredit;
					}else {
						if (sources.get("total_score") instanceof Integer){
							totalCredit = ((Integer) sources.get("total_score")).floatValue();
						}else if (sources.get("total_score") instanceof Double){
							totalCredit = ((Double) sources.get("total_score")).floatValue();
						}else {
							//不会走到这里
							totalCredit = dbCredit;
						}
					}

					totalCredit = totalCredit + diffCredit;

					ret.put("total_score",totalCredit);

					//修改数据
					bulkRequest.add(client.prepareUpdate(indexName,type,ret.get("user_id").toString()).setDoc(ret));
				}

				//记录日志
				this.getExecutor().submit(new Qbaolog(ret));

				indexTextLines.add(indexPoStr.toString());
				count++;

				System.out.println(System.currentTimeMillis() - start);
			}

			if (bulkRequest.numberOfActions() > 0 ){
				bulkRequest.execute().actionGet();
			}

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
		po.setDate("2017-11-21");
		po.setIndex(LoadValues.SCORE_INDEX);
		po.setTable("active_user_credit_sync_final_d");
		po.setKey("user_id");
		po.setDt("stat_date");
		//导入新闻类别
		IndexUpdate.getInstance().DoTask(po, false);

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


//		po.setDate("2017-09-18");
//		po.setIndex("sftimeall");
//		po.setTable("t_result_oneday_all_d");
//		po.setKey("user_id,cluster_id,module_id");
//		IndexUpdate.getInstance().DoTask(po, false);
		// logger.info(IndexUpdate.getInstance().taskLog);

		System.exit(0);



//		IndexUpdate indexUpdate = IndexUpdate.getInstance();
//
//		indexUpdate.GetEsClient();
//
//		GetRequestBuilder requestBuilder = indexUpdate.client.prepareGet("busscore","busscore","123")
//				.setFetchSource(new String[]{"task_score","credit_score"},null);
//
//		GetResponse response = requestBuilder.execute().actionGet();
//
//
//		System.out.println(response.getSourceAsMap());
//
//		System.exit(0);
	}

	class Qbaolog implements Runnable{
		private Map map;

		public Qbaolog(Map map) {
			this.map = map;
		}

		@Override
		public void run() {
			String logUrl = Config.get().get("logcenter.url");

			JSONObject toLog = new JSONObject();
			toLog.put("companyId", 101);
			toLog.put("productId", 10114);
			toLog.put("pageId", UUID.randomUUID());
			toLog.put("uid", map.get("user_id"));
			toLog.put("stamp", System.currentTimeMillis());
			toLog.put("data", JSON.toJSONString(map));

			try {
				String resut = HttpUtils.postByJson(logUrl, toLog.toJSONString());
				logger.info("脚本-信用总分进中央日志系统返回" + resut);
			} catch (UnsupportedEncodingException e) {
				logger.error("脚本-信用总分进进中央日志系统错误 error", e);
			}
		}
	}


	private static ExecutorService executor;
	private Object executorLock = new Object();
	protected ExecutorService getExecutor() {
		if (executor == null) {
			synchronized (executorLock) {
				if (executor == null) {
					executor = new ThreadPoolExecutor(
							2,
							4,
							60L,
							TimeUnit.SECONDS,
							new LinkedBlockingQueue<Runnable>(500)
					);
				}
			}
		}
		return executor;
	}


}

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
import java.util.List;
import java.util.Map;

import com.qbao.search.conf.LoadValues;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import util.PageConfig;

public class IndexUpdate implements Closeable {

	private static ESLogger logger = Loggers.getLogger(IndexUpdate.class);
	private static IndexUpdate indexUpdate;
	private static String user = Config.getBase().get(LoadValues.SEARCHDB_USERNAME).trim();
	private static String pwd = Config.getBase().get(LoadValues.SEARCHDB_PASSWORD).trim();
	private static String url = Config.getBase().get(LoadValues.CONFIG_CONNECTION).trim();
	//private static String SQL = Config.get().get("es.getdata.sql", "select * from user_label");
	//private static String SQL_ORDER = Config.get().get("es.getdata.sql.order", "order by id desc ");
	//private static String SQL_COUNT = Config.get().get("es.getdata.sql.count", "select count(*) from user_label");
	//private static String INDEX_NAME = Config.get().get("es.index.name", "userlabel");
	//private static String INDEX_TYPE_NAME = Config.get().get("es.index.type.name", "user_label");
	//private static String esIndexKeys = Config.get().get("es.key.field.name", "e_user_id");
	/** conn链接过期时间 **/
	//private int CONNECT_Time_OUT = Config.get().getInt("es.connect.timeout.second", 1800);
	//private int pageSize = Config.get().getInt("es.index.import.pagesize", 50000);
	/** 本机备份文件路径 **/
	private static String backupFilePath = System.getProperty("user.dir") + File.separator + "ms_es_file_bk_";
	private static Connection conn;
	private static TransportClient client;
	// private static BulkRequestBuilder bulkRequest;

	volatile boolean is_last_page = false;
	/** 记录总数 */
	private int totalResultSize = 0;
	/** 总页数 */
	private int totalPageNum;;
	/** 分页容器 **/
	private PageConfig<Object> pageConfig;

	// 任务记录log
	private static StringBuffer taskLog = new StringBuffer();

	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private String getTodayDateString() {
		return sdf.format(new Date());
	}

	private String getYestodayDateString() {
		Date yestoday = new Date(new Date().getTime() - 84000 * 1000);
		return sdf.format(yestoday);
	}

	public static final IndexUpdate getInstance() {
		try {
			if (indexUpdate == null) {
				synchronized (IndexUpdate.class) {
					// GetEsClient();

					// bulkRequest = client.prepareBulk();
					// -----------------Mysql连接--------------------
					// String driver = "com.mysql.jdbc.Driver";
					// String user = Config.get().get("db.deyouOnline.user");
					// String pwd = Config.get().get("db.deyouOnline.pass");
					// String url
					// =Config.get().get("db.deyouOnline.connection");
					// conn = DriverManager.getConnection(url, user, pwd);

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
	private boolean getPageConfig(String date) {
		/*PreparedStatement st;
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			st = conn.prepareStatement(SQL_COUNT + " where dt='" + date + "'");
			logger.info("getPageConfig.url"+url);
			logger.info("getPageConfig.sql"+SQL_COUNT + " where dt='" + date + "'");
			st.setQueryTimeout(CONNECT_Time_OUT);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				taskLog.append(new Date().toLocaleString() + "---------->select count(*) from mysql success, results=="
						+ rs.getInt(1) + "<br/>");
				logger.info(new Date().toLocaleString() + "---------->select count(*) from msmysql success, results=="
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
		logger.info(new Date().toLocaleString() + "//fail to index :406-3");*/
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
	public synchronized String DoTask(String date, boolean fileBackup) {


		return "";
	}

	/**
	 * 从数据库批量导入数据
	 * 
	 * @throws Exception
	 */
	private void IndexBathImport(String indexName, String type, String date, long startLine, long lastLine,
			boolean fileBackup) {/*
		PreparedStatement st;
		GetEsClient();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		*//** 本地备份用 **//*
		ArrayList<String> indexTextLines = new ArrayList<String>();
		String indexTxtFileName = date + ".txt";
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			st = conn.prepareStatement(
					SQL + " where dt='" + date + "' " + SQL_ORDER + "  limit " + startLine + "," + lastLine);

			st.setQueryTimeout(CONNECT_Time_OUT);

			ResultSet rs = st.executeQuery();
			logger.info(new Date() + "------>select from mysqlms success , page=" + this.pageConfig.getCurrentPage()
					+ ",pageSize=" + this.pageSize);
			taskLog.append(new Date() + "------>select from mysqlms success , page=" + this.pageConfig.getCurrentPage()
					+ ",pageSize=" + this.pageSize + "<br/>");
			Map<String, Object> ret = new LinkedHashMap<String, Object>();
			String columName;
			int count = 0;
			String splitStr = "";// 处理逗号分隔符
			while (rs.next()) {
				StringBuffer indexPoStr = new StringBuffer();

				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					columName = rs.getMetaData().getColumnName(i);
					// System.out.println(columName+"="+rs.getMetaData().getColumnTypeName(i).trim());
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
						*//**
						 * java.lang.Boolean \java.lang.Byte \java.lang.Short
						 * java.lang.Double\ java.sql.Time \java.sql.Timestamp
						 * java.sql.Blob\ java.sql.Clob对没有加入的类型一律用sting 做索引
						 **//*
						

						ret.put(columName, rs.getString(columName));

						indexPoWrap(indexPoStr, rs.getMetaData().getColumnTypeName(i).trim(), columName,
								rs.getString(columName));
						break;
					}

				}

				bulkRequest.add(client.prepareIndex(indexName, type).setId(rs.getString(esIndexKeys)).setSource(ret));
				indexTextLines.add(indexPoStr.toString());
				count++;
			}
			BulkResponse re = bulkRequest.execute().actionGet();
			logger.info(new Date() + "------> es index batch input page=" + this.pageConfig.getCurrentPage()
					+ ",records=" + count);
			taskLog.append(new Date() + "------> es index batch input page=" + this.pageConfig.getCurrentPage()
					+ ",records=" + count + "<br/>");

			if (fileBackup) {

				util.FileUtil.inputTXT(backupFilePath, indexTxtFileName, indexTextLines, true);
				logger.info(new Date() + "------> es index backup File,path=" + backupFilePath + indexTxtFileName
						+ ",page=" + this.pageConfig.getCurrentPage() + ",records=" + indexTextLines.size() + "<br/>");
				taskLog.append(new Date() + "------> es index backup File,path=" + backupFilePath + indexTxtFileName
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
*/
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

		IndexUpdate.getInstance().DoTask("2017-06-04", false);
		// logger.info(IndexUpdate.getInstance().taskLog);

	}

}

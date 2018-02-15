package data.db;

import com.alibaba.fastjson.JSON;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import vo.EsDictPo;
import vo.EsDictPo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class EsDataServiceSql {
	private static final ESLogger logger = Loggers.getLogger(EsDataServiceSql.class);
	private static EsDataServiceSql Instance;
	private String dbconnect;
	private String dbUserName;
	private String dbPassword;
	//private String sql_Select;
	//private String sql_Count;
	private Connection con = null;
	final int batchSize = 10000;

	public static final EsDataServiceSql getInstance() {
		if (Instance == null) {
			synchronized (EsDataServiceSql.class) {
				if (Instance == null) {
					Instance = new EsDataServiceSql();
				}
			}
		}
		return Instance;
	}

	private EsDataServiceSql() {
		super();
		try {
			this.dbconnect 	  = Config.getBase().get(LoadValues.CONFIG_CONNECTION);
			this.dbUserName	  = Config.getBase().get(LoadValues.SEARCHDB_USERNAME).trim();
			this.dbPassword   = Config.getBase().get(LoadValues.SEARCHDB_PASSWORD).trim();

			//this.sql_Select = Config.get().get("recommend.sql.query.select");
			//this.sql_Count = Config.get().get("recommend.sql.query.count");
		} catch (Exception e) {
			logger.info(e);
		}

	}

	public synchronized ArrayList<EsDictPo> select(String sql) {
		long now = System.currentTimeMillis();
		ArrayList<EsDictPo> results = new ArrayList<EsDictPo>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (con == null) {
				con = DriverManager.getConnection(dbconnect, dbUserName, dbPassword);
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			results = sqlPack(rs);
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if(con!=null) {
					con.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			con = null;
		} finally {
			rs = null;
			stmt = null;
			con = null;
		}

		logger.info("加载字典数据成功,耗时time=" + (System.currentTimeMillis() - now) + "ms, sum = " + results.size());

		return results;
	}

	public synchronized int count(String sql) {
		int sum = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (con == null) {
				con = DriverManager.getConnection(dbconnect, dbUserName, dbPassword);
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sum = rs.getInt(1);
			}
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if(con!=null) {
					con.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			con = null;
		} finally {
			rs = null;
			stmt = null;
			con = null;
		}
		return sum;
	}

	private ArrayList<EsDictPo> sqlPack(ResultSet rs) throws SQLException {
		ArrayList<EsDictPo> results = new ArrayList<EsDictPo>();
		while (rs.next()) {
			EsDictPo po = new EsDictPo();
			po.setId(rs.getInt("id"));
			po.setParentName(rs.getString("parent_name"));
			po.setLabelName(rs.getString("label_name"));
			po.setOption(JSON.parseArray(rs.getString("option")));
			po.setGrafType(rs.getString("graf_type"));
			po.setLabelId(rs.getString("label_id"));
			po.setLabelType(rs.getInt("label_type"));

			results.add(po);

		}
		return results;
	}

	/*public String getSql_Select() {
		return sql_Select;
	}

	public void setSql_Select(String sql_Select) {
		this.sql_Select = sql_Select;
	}*/

	/*public String getSql_Count() {
		return sql_Count;
	}

	public void setSql_Count(String sql_Count) {
		this.sql_Count = sql_Count;
	}
*/

	public static void main(String[] args) throws Exception, IOException {


	}

}
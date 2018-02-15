package com.qbao.search.sql;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import com.qbao.search.conf.Config;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-12-8 16:23:38
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-8 16:23:38  	li_yao		1.0			Newly created
 */
public class DBUtil {
	
	public static Connection getConnection(String conKey, String usrKey, 
			String pwdKey) throws SQLException, IOException{
		String url = Config.get().get(conKey);
		if(url == null || url.isEmpty()) {
			url = Config.getBase().get(conKey);
		}
		String user = Config.getBase().get(usrKey);
		String password = Config.getBase().get(pwdKey);
		return DriverManager.getConnection(url, user, password);
	}
	
	public static Connection getCollectorConnection() 
			throws SQLException, IOException {
		return DBUtil.getConnection(
			Config.get().get("collector.db.connection.key", "db.searchdb.connect"),
			Config.get().get("collector.db.user.key", "searchdb.username"),
			Config.get().get("collector.db.password.key", "searchdb.password")
		);
	}
	
	public static Connection getConnection() throws SQLException, IOException{
		//String url = Config.get().get("db.searchdb.connect");
		String url = Config.getBase().get("db.loadconfig.connection");
		
		String user =
				Config.getBase().get("searchdb.username");
		String password = 
				Config.getBase().get("searchdb.password");
		return DriverManager.getConnection(url, user, password);
	}
	
	public static Connection getLogConnection() 
			throws SQLException, IOException {
		String url = Config.getBase().get("db.searchlogdb.connect");
		
		String user = 
				Config.getBase().get("searchlog.username");
		String password = 
				Config.getBase().get("searchlog.password");
		return DriverManager.getConnection(url, user, password);
	}
	
	public static PreparedStatement getInsertStatement(Connection connection,
			String tableName, String[] columns) throws SQLException {
		return getInsertStatement(connection, tableName, columns, 1);
	}
	
	public static PreparedStatement getInsertStatement(Connection connection,
			String tableName, String[] columns, int insertNum)
				throws SQLException {
		StringBuilder paramSb = new StringBuilder(2 + columns.length*2);
		paramSb.append('(');
		StringBuilder sqlSb = new StringBuilder(columns.length * 15 + 
				insertNum * paramSb.capacity());
		sqlSb.append("insert into ");
		sqlSb.append(tableName);
		sqlSb.append(" (");
		for(String column:columns){
			paramSb.append("?,");
			sqlSb.append(column);
			sqlSb.append(',');
		}
		paramSb.setLength(paramSb.length() - 1);
		paramSb.append("),");
		sqlSb.setLength(sqlSb.length() - 1);
		sqlSb.append(") values ");
		for(int i = 0; i < insertNum; i++){
			sqlSb.append(paramSb);
		}
		sqlSb.setLength(sqlSb.length() - 1);
		return connection.prepareStatement(sqlSb.toString());
	}
	
	public static CallableStatement getCallableStatement(Connection connection,
			String spName, int columnNum) throws SQLException {
		if(columnNum < 1){
			throw new IllegalArgumentException("columnNum: " + columnNum + 
					" is less than 1");
		}
		StringBuilder sqlSb = new StringBuilder(20 + columnNum * 2);
		sqlSb.append("{call ");
		sqlSb.append(spName);
		sqlSb.append('(');
		for(int i = 0; i < columnNum; i++){
			sqlSb.append('?');
			sqlSb.append(',');
		}
		sqlSb.setLength(sqlSb.length() - 1);
		sqlSb.append(')');
		sqlSb.append('}');

		return connection.prepareCall(sqlSb.toString());
	}
	
	public static String[] get(Connection connection, String sql) 
			throws SQLException {
		Statement statement = null;
		ResultSet rs = null;
		try{
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if(rs.next()) {
				String[] arr = new String[rs.getMetaData().getColumnCount()];
				for(int i = 0; i < arr.length; i++) {
					arr[i] = rs.getString(i + 1);
				}
				return arr;
			}
			return null;
		} finally {
			try{
				if(rs != null){
					rs.close();
				}
			} finally {
				if(statement != null){
					statement.close();
				}
			}
		}
	}
	
	public static String getString(Connection connection, String sql) 
			throws SQLException {
		String[] arr = get(connection, sql);
		return arr == null ? null : arr[0];
	}
	
	public static int getInt(Connection connection, String sql) 
			throws SQLException {
		return Integer.parseInt(get(connection, sql)[0]);
	}
	
	public static long getLong(Connection connection, String sql) 
			throws SQLException {
		return Long.parseLong(get(connection, sql)[0]);
	}
	
	public static abstract class DBTask<T> implements Callable<T> {
		final Connection connection;
		final String sql;
		public DBTask(Connection connection, String sql) {
			this.connection = connection;
			this.sql = sql;
		}

		protected abstract T execute(ResultSet rs) throws Exception;
		
		final public T call() throws Exception {
			Statement statement = null;
			ResultSet rs = null;
			try {
				statement = connection.createStatement();
				rs = statement.executeQuery(sql);
				return execute(rs);
			} finally{				
				try{
					if(connection != null){
						connection.close();
					}
				} finally {
					try{
						if(statement != null){
							statement.close();
						}
					} finally {
						if(rs != null){
							rs.close();
						}
					}
				}			

			}
		}		
	}
	
}

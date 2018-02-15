package com.qbao.search.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

public class DBConfig {
	private static final ESLogger logger = Loggers.getLogger(DBConfig.class);
	
	public static long getConfValueLong (String confKey, long defaultValue){
        String value = getConfValue(confKey);
        if (value == null||value.equals("")){
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
	public static int getConfValueInt (String confKey, int defaultValue){
        String value = getConfValue(confKey);
        if (value == null||value.equals("")){
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
	/**
	 * 获取配置值
	 * @param confKey
	 * @return
	 */
	public static String getConfValue(String confKey){
		int sysNo=0;
		sysNo = Config.getBase().getInt("sysno", 901001);
		return getConfValue(sysNo,confKey);
	}
	public static String getConfValue(String confKey,String defaultValue){		 
		String value = getConfValue(confKey);
        if (value == null||value.equals("")){
            return defaultValue;
        }
		return value;
	}	
	private static String getConfValue(int sysNo,String confKey) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String valueString="";
		try {
	    	con = DBUtil.getConnection();
	    	String SQL = " select top 1 confvalue from configs with(nolock)   where ConfKey='"+confKey+"' and  SysNo="+sysNo;	    	 
	    	stmt = con.createStatement();
	    	rs = stmt.executeQuery(SQL);	    	   	 
	    	while (rs.next()){
	    		valueString=rs.getString(1);
	    		break;
	    	} 
		}
		catch (Exception e) 
		{ 
			logger.error(" getConfigValue 数据库读取 Configs 出错"+e.getMessage(),e);
		}
		finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
	    		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	    		if (con != null) try { con.close(); } catch(Exception e) {}
		}
		return valueString;
	}
	
	public static void setConfValue(String confKey, String confValue){
		int sysNo=0;
		sysNo = Config.getBase().getInt("sysno", 901001);
		setConfValue(sysNo,confKey,confValue);
	}
	
	private static void setConfValue(int sysNo,String confKey,String confValue){
		CallableStatement statement = null;
    	Connection conn=null;
    	try{ 
    		conn=DBUtil.getConnection();
    		Statement stmt = null;
    		ResultSet rs = null;
    		String SQL = " select top 1 ItemID from configs (nolock)   where ConfKey='"+confKey+"' and  SysNo="+sysNo;
    		stmt = conn.createStatement();
	    	rs = stmt.executeQuery(SQL);
	    	int itemID=0;
	    	while (rs.next()){
	    		itemID=rs.getInt(1);
	    		break;
	    	} 
	    	if (rs != null) try { rs.close(); } catch(Exception e) {}
    		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
    		//    		
	    	String[] columns=new String[]{"ItemID","datatype","confkey","confvalue","comment"};	    	
	    	statement=DBUtil.getCallableStatement(conn, "sp1_configs_u", columns.length);
	    	statement.setInt	(1, itemID);
			statement.setString	(2, "");
			statement.setString	(3, confKey);
			statement.setString	(4, confValue);
			statement.setString	(5, "");
			statement.addBatch();
			statement.executeBatch();    	
    	} catch (Exception e) {
    		logger.error(e);
    	} finally{
			if(statement != null)
			{
				try 
				{
					statement.close();
				} 
				catch (SQLException e) 
				{
					logger.error(e);
				}
			}
			if (conn != null)
			{
				try 
				{
					conn.close(); 
				} 
				catch(Exception e) 
				{
					logger.error(e);
				}
			}
    	}
	}
}

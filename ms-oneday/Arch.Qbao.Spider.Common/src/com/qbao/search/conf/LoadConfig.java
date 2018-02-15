package com.qbao.search.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <br>
 * 
 * Copyright 2012 Ctrip.com, Inc. All rights re	served.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.<br>
 *
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	ColumnInfo.java<br>
 *
 * Author:      MENG Wenyuan (wymeng@ctrip.com)<br>
 * ColumnInfo<br>
 * Create Date: 2011-11-30<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public class LoadConfig {
	 
	private static LoadConfig SINGLETON ;
	
	private Config 		configBase;
	private Config 		config;

	private String      dbconnect;
	private String	    dbUserName;
	private String      dbPassword;
	
	private String[] 	saveFileName;
	private String[] 	oldSource;
	private String[]    sqlQuery;
	
	private ScheduledExecutorService  service;
	private Connection 	con = null;
	
	private AtomicBoolean isLoaded;
	
	public static Map<String, String> userMap = new HashMap<String, String>();
	public static Map<String, JSONObject> dictMap = new HashMap<String, JSONObject>();
	public static Map<String, JSONArray> dictOptionMap = new HashMap<String, JSONArray>();
	public static Map<String, String> dictCodeName = new HashMap<String, String>();
	public static Map<String, String> dictCodeType = new HashMap<String, String>();
	public static Map<String, JSONArray> dictAll = new HashMap<String, JSONArray>();
	public static Map<String, String> areas = new HashMap<String, String>();
	public static Map<String, String> citys = new HashMap<String, String>();
	public static Map<String, Map<String,String>> dictOption = new HashMap<String, Map<String,String>>();
	/**
	 * 所有properties，放入内存，定时刷新
	 */
	public static Map<String, String> properties = new HashMap<String, String>();
	/***************************************************************
	 * @method�?get<br>
	 * <br>
	 * @return
	 * @throws FileNotFoundException
	 * <br>
	 */
	public static LoadConfig get() throws FileNotFoundException, Exception{
		synchronized(LoadConfig.class){
			if(SINGLETON == null){
				if(Config.getBase().getBoolean("deploy.server.config", false))
                    SINGLETON = new EmptyLoadConfig();
                else
                    SINGLETON = new LoadConfig();
			}
		}
		return SINGLETON;
	}
	static class EmptyLoadConfig extends LoadConfig{
		 public void load(boolean flag){
		 }
		 public EmptyLoadConfig()
		 			throws FileNotFoundException, Exception{
			 super(false);
		 }
	}
	 private LoadConfig(boolean isDeploy) throws FileNotFoundException, Exception{
		 con = null;
	 }
	/***************************************************************
	 * @Constructor�?LoadConfig.java - LoadConfig<br>
	 * <br>
	 * @throws FileNotFoundException
	 * <br>
	 */
	private LoadConfig() throws FileNotFoundException, Exception{
		configBase   	  = Config.getBase();
		config       	  = Config.get();
		
		this.saveFileName = new String[]{ LoadValues.FILE_MAIN,
										  LoadValues.FILE_LOG4J,
										LoadValues.FILE_SUB};
		
		this.oldSource    = new String[]{"","",""};
		this.service  	  = Executors.newScheduledThreadPool(2);
		int intervalTime = configBase.getInt("db.loadconfig.intervaltime", 300);
		
		for (int i =0; i<2; i++){
			if (intervalTime > 0)
				service.scheduleWithFixedDelay(new LoadTask(i), 0, intervalTime, TimeUnit.SECONDS);
			else service.execute(new LoadTask(i));
		}

		this.isLoaded = new AtomicBoolean(false);
	}

	
	/***************************************************************
	 * @method： load<br>
	 * <br>
	 * @param isMain<br>
	 * <br>
	 */
	public void load(boolean isMain) {
		//load(isMain ? 0 : 1);
	}
	
	
	/***************************************************************
	 * @method： load<br>
	 * <br>
	 * @param pos<br>
	 * <br>
	 */
	private void load(int pos) {
	}


	private void loadPropertiesTable(int pos) {
		StringBuffer source = new StringBuffer("\n");
		synchronized(this){
			Statement stmt = null;
			ResultSet rs   = null;
			try {
				if (con == null){
					con = DriverManager.getConnection( dbconnect,dbUserName,dbPassword);
				}
				stmt = con.createStatement();

				rs = stmt.executeQuery(LoadValues.SQL_PROPERTIES_QUERY);

				while (rs.next()) {
					String key = rs.getString("property_name");
					String value = rs.getString("property_value");
					properties.put(key, value);
					String comment 	= rs.getString("remark");
					if (comment !=null && !comment.equalsIgnoreCase(""))
						source.append("#").append(comment).append("\n");
					source.append(key).append(" = ").append(value).append("\n\r");
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				con = null;
			} finally{
				rs 	 = null;
				stmt = null;
			}
		}
		String newSource = source.toString();
		System.out.println(new Date().toString()+"生成配置文件！");
		if (!newSource.equals("\n") && !oldSource[pos].equalsIgnoreCase(newSource)){
			try {
				doFile(LoadValues.FILE_PATH + saveFileName[pos], newSource);
				oldSource[pos]=newSource;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 数据字典初始化添加

		
	}

	private void loadPropertiesByParent(int pos) {
		StringBuffer source = new StringBuffer("\n");
		String _key="home.page";
		synchronized(this){
			PreparedStatement stmt = null;
			ResultSet rs   = null;
			try {
				if (con == null){
					con = DriverManager.getConnection( dbconnect,dbUserName,dbPassword);
				}
				stmt = con.prepareStatement(LoadValues.SQL_PROPERTIES_BY_PARENT);
				stmt.setString(1,_key);
				rs = stmt.executeQuery();
				JSONArray array=new JSONArray();
				while (rs.next()) {
					String key = rs.getString("property_name");
					String value = rs.getString("property_value");
					String comment 	= rs.getString("remark");
					JSONObject obj= new JSONObject();
					obj.put("property_name",key);
					obj.put("property_value",value);
					obj.put("remark",comment);
					array.add(obj);
				}
				if (array.size() > 0)
					source.append("#").append("index.info").append("\n");
				source.append(_key).append(" = ").append(array.toString()).append("\n\r");
				properties.put(_key, array.toString());
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				con = null;
			} finally{
				rs 	 = null;
				stmt = null;
			}
		}
		String newSource = source.toString();
		System.out.println("生成配置文件！");
		if (!newSource.equals("\n") && !oldSource[pos].equalsIgnoreCase(newSource)){
			try {
				doFile(LoadValues.FILE_PATH + saveFileName[pos], newSource);
				oldSource[pos]=newSource;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 数据字典初始化添加


	}

	/**
	 * 
	 * <br>
	 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
	 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
	 *<br>
	 * Projet Name:	Arch.Search.Common<br>
	 * File Name:	LoadConfig.java<br>
	 *
	 * Author:      MENG Wenyuan (wymeng@ctrip.com)<br>
	 * Create Date: 2011-12-20<br>
	 * Version:		1.0<br>
	 * Modification:<br><br>
	 */
	class LoadTask implements Runnable{
		
		
		private int myPos = 0;
		
		/***************************************************************
		 * @Constructor： LoadConfig.java - LoadTask<br>
		 * <br>
		 * @param p<br>
		 * <br>
		 */
		LoadTask(int p){
			this.myPos = p;
		}
	
		
		/***************************************************************
		 * @method： run<br>
		 * (non-Javadoc)<br>
		 * run - LoadTask<br>
		 * <br>
		 */
		public void run() {
			try {
				load(myPos);
				//loadPropertiesTable(0);
				//loadPropertiesByParent(2);
			} catch(Exception e) {
				//suppress it so that subsequent task won't be suppressed
				e.printStackTrace();
			}
		}
	}
	 
	/***************************************************************
	 * @method�?doFile<br>
	 * <br>
	 * @param source
	 * <br>
	 */
	private void doFile(String fileName, String source) throws IOException{
		FileOutputStream out 		= null;    
        BufferedOutputStream Buff	= null;     
        source ="#"+ (new Date()) + "\n\r" + source;
        out = new FileOutputStream(new File(fileName));    
        
        Buff=new BufferedOutputStream(out);    
        long begin0 = System.currentTimeMillis();    
        Buff.write(source.getBytes());    
             
        Buff.flush();    
        Buff.close();    
        long end0 = System.currentTimeMillis();    
       System.out.println(fileName + " 执行耗时:" + (end0 - begin0) + " 豪秒");    
	}

	
	/***************************************************************
	 * @method： releaseSource<br>
	 * <br><br>
	 * <br>
	 */
	public void releaseSource() {
		service.shutdownNow();
	}
	
	public static Map<String, String> getUserMap() {
		return userMap;
	}
	public static void setUserMap(Map<String, String> userMap) {
		LoadConfig.userMap = userMap;
	}
}
	 

		 

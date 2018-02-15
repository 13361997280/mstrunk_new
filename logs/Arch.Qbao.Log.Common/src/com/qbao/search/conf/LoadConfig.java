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
	public static Map<String, String> newsSet = new HashMap<String, String>();
	public static Map<String, String> newsSet1 = new HashMap<String, String>();
	public static Map<String, String> newsSetItems = new HashMap<String, String>();
	public static Map<String, String> newsCategroy = new HashMap<String, String>();
	public static Map<String, String> categroyNews = new HashMap<String, String>();
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
		this.dbconnect 	  = configBase.get(LoadValues.CONFIG_CONNECTION);
		this.dbUserName	  = configBase.get(LoadValues.SEARCHDB_USERNAME).trim();
		this.dbPassword   = configBase.get(LoadValues.SEARCHDB_PASSWORD).trim();

		this.saveFileName = new String[]{LoadValues.FILE_LOG4J};
		
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
		load(isMain ? 0 : 1);
	}
	
	
	/***************************************************************
	 * @method： load<br>
	 * <br>
	 * @param pos<br>
	 * <br>
	 */
	private void load(int pos) {
/*
		StringBuffer source = new StringBuffer("\n");
		synchronized (this) {

			Statement stmt = null;
			ResultSet rs = null;

			// 用户信息初始化添加
			try {
				if (con == null) {
					con = DriverManager.getConnection(dbconnect, dbUserName, dbPassword);
				}
				stmt = con.createStatement();

				rs = stmt.executeQuery(LoadValues.SQL_QUERY);

				while (rs.next()) {
					String key = rs.getString("appid");
					String value = rs.getString("secret") + ":" + rs.getString("id");
					// 初始化加载所有的用户信息进入内存中
					userMap.put(key, value);
					source.append(key).append(" = ").append(value).append("\n\r");
				}
				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.USER_DICT_PARENT).trim());
				JSONArray menus = new JSONArray();
				while (rs.next()) {
					JSONObject o = new JSONObject();
					o.put("name", rs.getString("parent_name"));
					o.put("icon", rs.getString("icon"));
					Statement stmt1 = con.createStatement();
					ResultSet rs1 = stmt1.executeQuery(config.get(LoadValues.USER_LABEL_DICT_SQL).trim() + " and parent_name ='" + rs.getString("parent_name") + "'");
					JSONArray j = new JSONArray();
					while (rs1.next()) {
						JSONObject o1 = new JSONObject();
						o1.put("id", rs1.getInt("id"));
						o1.put("code", rs1.getString("label_id"));
						o1.put("name", rs1.getString("label_name"));
						o1.put("defaultValue", rs1.getString("default_value"));
						o1.put("type", rs1.getInt("label_type"));
						o1.put("isShow", rs1.getBoolean("can_filter"));
						o1.put("isReport", rs1.getBoolean("can_report_table"));
						o1.put("order", rs1.getInt("sort"));
						String option = rs1.getString("option");
						if(null!=rs1.getString("option")&&!"".equals(rs1.getString("option"))){
							JSONArray jsonArray = JSON.parseArray(rs1.getString("option"));
							Map<String,String> map = new HashMap<String, String>();
							for(int i=0;i<jsonArray.size();i++){
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								map.put(jsonObject.getString("value"),jsonObject.getString("name"));
							}
							dictOption.put(rs1.getString("label_id"),map);
						}
						o1.put("options", JSON.parseArray(option));
						j.add(o1);
						dictOptionMap.put(rs1.getString("label_id"), JSON.parseArray(rs1.getString("option")));
						dictCodeName.put(rs1.getString("label_id"), rs1.getString("label_name"));
						dictCodeType.put(rs1.getString("label_id"), rs1.getString("label_type"));

					}
					if (j.size() > 0) {
						o.put("list", j);
						menus.add(o);
					}
				}
				JSONObject j2 = new JSONObject();
				j2.put("menus", menus);

				dictMap.put("menus", j2);


				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.USER_LABEL_DICT_SQL).trim());
				JSONArray menus1 = new JSONArray();
				while (rs.next()) {
					JSONObject o1 = new JSONObject();
					o1.put("labelName", rs.getString("label_name"));
					o1.put("parentName", rs.getString("parent_name"));
					o1.put("groupName", rs.getString("group_name"));
					o1.put("labelId", rs.getString("label_id"));
					o1.put("labelType", rs.getString("label_type"));
					o1.put("icon", rs.getString("icon"));
					menus1.add(o1);
				}
				dictAll.put("menus", menus1);

				//加载地区信息入内存
				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.AREA_SQL).trim());
				while (rs.next()) {
					String key = rs.getString("code");
					String value = rs.getString("name");
					// 初始化加载所有的用户信息进入内存中
					areas.put(key, value);
				}

				//加载城市信息入内容
				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.CITY_SQL).trim());
				while (rs.next()) {
					String key = rs.getString("code");
					String value = rs.getString("name");
					// 初始化加载所有的用户信息进入内存中
					citys.put(key, value);
				}

				//新闻资讯设置
				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.NEWS_SQL).trim());
				while (rs.next()) {
					String key = rs.getString("id");
					String value = "推荐,"+rs.getString("items")+","+rs.getString("more")+","+rs.getString("other");
					// 初始化加载所有的用户信息进入内存中
					newsSet.put(key, value);
					newsSetItems.put(key, rs.getString("items"));
					Integer ageLow = rs.getInt("age_low");
					Integer ageHigh = rs.getInt("age_high");
					Integer sex = rs.getInt("sex");
					Integer ageT = ageHigh-ageLow;
					for(int i=0;i<=ageT;i++){
						Integer age = ageLow+i;
						String key1 = age+","+sex;
						newsSet1.put(key1, key);
					}
				}

				//新闻类型设置
				stmt = con.createStatement();
				rs = stmt.executeQuery(config.get(LoadValues.NEWS_TYPE_SQL).trim());
				while (rs.next()) {
					String code = rs.getString("code");
					String name = rs.getString("name");
					// 初始化加载所有的用户信息进入内存中
					newsCategroy.put(code, name);
					categroyNews.put(name, code);
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
			} finally {
				rs = null;
				stmt = null;
			}
		}*/
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
	 

		 

package com.qbao.search.conf;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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

	private String      dbconnect;
	private String	    dbUserName;
	private String      dbPassword;

	private ScheduledExecutorService  service;
	private Connection 	con = null;

	public static Map<String, Double> sign = new HashMap<String, Double>();
	public static Map<String, Integer> bus = new HashMap<String, Integer>();
	public static Map<String, Double> sys = new HashMap<String, Double>();
	public static Map<Integer, JSONObject> task = new HashMap<Integer, JSONObject>();
	public static List<JSONObject> item = new ArrayList<JSONObject>();
	public static Map<String, Double> taskSet = new HashMap<String, Double>();
	public static Map<Double, Double> ratio = new HashMap<Double, Double>();


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
		this.dbconnect 	  = configBase.get(LoadValues.CREDIT_CONNECTION);
		this.dbUserName	  = configBase.get(LoadValues.CREDIT_USERNAME).trim();
		this.dbPassword   = configBase.get(LoadValues.CREDIT_PASSWORD).trim();

		this.service  	  = Executors.newScheduledThreadPool(2);
		int intervalTime = configBase.getInt("db.loadconfig.intervaltime", 300);
		
		for (int i =0; i<2; i++){
			if (intervalTime > 0)
				service.scheduleWithFixedDelay(new LoadTask(i), 0, intervalTime, TimeUnit.SECONDS);
			else service.execute(new LoadTask(i));
		}

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

		StringBuffer source = new StringBuffer("\n");
		synchronized (this) {

			Statement stmt = null;
			ResultSet rs = null;

			// 用户信息初始化添加
			/*try {
				if (con == null) {
					con = DriverManager.getConnection(dbconnect, dbUserName, dbPassword);
				}

				stmt = con.createStatement();
				rs = stmt.executeQuery("select `score`,`total_score_limit` from conf_sign where `status` = 0 ");
				while (rs.next()) {
					sign.put("signScore", rs.getDouble("score"));
					sign.put("signScoreLimit", rs.getDouble("total_score_limit"));
				}

				stmt = con.createStatement();
				rs = stmt.executeQuery("select `bus_id`,`bus_name` from conf_bus where `status` = 0 ");
				while (rs.next()) {
					bus.put(rs.getString("bus_name"), rs.getInt("bus_id"));
				}

				stmt = con.createStatement();
				rs = stmt.executeQuery("select `oneday_score_limit` from conf_base_set  ");
				while (rs.next()) {
					sys.put("onedayScoreLimit", rs.getDouble("oneday_score_limit"));
				}

				stmt = con.createStatement();
				rs = stmt.executeQuery("select `task_id`,`add_score`,`sub_score`,`is_duplicate` from conf_task where `status` = 0  ");
				while (rs.next()) {
					Integer task_id = rs.getInt("task_id");
					Double add_score = rs.getDouble("add_score");
					Double sub_score = rs.getDouble("sub_score");
					String is_duplicate = rs.getString("is_duplicate");
					JSONObject object1 = new JSONObject();
					object1.put("taskAddScore",add_score);
					object1.put("taskSubScore",sub_score);
					object1.put("isDuplicate",is_duplicate);
					task.put(task_id, object1);
				}

				stmt = con.createStatement();
				rs = stmt.executeQuery("select `oneday_score`,`total_score` from conf_task_set");
				while (rs.next()) {
					Double oneday_score = rs.getDouble("oneday_score");
					Double total_score = rs.getDouble("total_score");
					taskSet.put("todayTaskScoreLimit", oneday_score);
					taskSet.put("taskScoreLimit", total_score);
				}
				stmt = con.createStatement();
				rs = stmt.executeQuery("select `start_score`,`ratio` from conf_ratio where `status` = 0  ");
				while (rs.next()) {
					Double start_score = rs.getDouble("start_score");
					Double ratioR = rs.getDouble("ratio");
					ratio.put(start_score, ratioR);
				}
				item = new ArrayList<JSONObject>();
				stmt = con.createStatement();
				rs = stmt.executeQuery("select `bus_name`,`title`,`sub_title`,`image_url`,`link_url`,`class_name`,`class_type` from conf_item where `status` = 0  ");
				JSONObject jsonObject = null;
				while (rs.next()) {
					String bus_name = rs.getString("bus_name");
					String title = rs.getString("title");
					String sub_title = rs.getString("sub_title");
					String image_url = rs.getString("image_url");
					String link_url = rs.getString("link_url");
					Integer class_type = rs.getInt("class_type");
					String class_name = rs.getString("class_name");
					jsonObject = new JSONObject();
					jsonObject.put("title",title);
					jsonObject.put("subTitle",sub_title);
					jsonObject.put("imageUrl",image_url);
					jsonObject.put("linkUrl",link_url);
					jsonObject.put("busName",bus_name);
					jsonObject.put("className",class_name);
					jsonObject.put("classType",class_type);
					item.add(jsonObject);
				}
				rs.close();
				stmt.close();

			} catch (Exception e) {
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
			}*/
		}
	}
	private Double[] convertDoubleArray(String str){
		String[] strArray = str.split(",");
		Double[] doubleArray = new Double[strArray.length];
		for(int i=0;i<strArray.length;i++){
			doubleArray[i] = Double.parseDouble(strArray[i]);
		}
		return doubleArray;
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
			} catch(Exception e) {
				//suppress it so that subsequent task won't be suppressed
				e.printStackTrace();
			}
		}
	}

	
	/***************************************************************
	 * @method： releaseSource<br>
	 * <br><br>
	 * <br>
	 */
	public void releaseSource() {
		service.shutdownNow();
	}

}
	 

		 

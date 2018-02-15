package com.qbao.search.conf;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
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

	public static Map<String, String> newsCategroy = new HashMap<String, String>();
	public static Map<String, String> categroyNews = new HashMap<String, String>();
	public static Map<String, String> defaultNewsType = new HashMap<String, String>();
	//advertisement key为  type:pageNo  type为广告类型 1为新闻列表 2位新闻详情 pageNo为第几页
	public static Map<String, JSONObject> adv = new HashMap<String, JSONObject>();


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
		this.dbconnect 	  = configBase.get(LoadValues.ONEDAY_CONNECTION);
		this.dbUserName	  = configBase.get(LoadValues.ONEDAY_USERNAME).trim();
		this.dbPassword   = configBase.get(LoadValues.ONEDAY_PASSWORD).trim();

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
			try {
				if (con == null) {
					con = DriverManager.getConnection(dbconnect, dbUserName, dbPassword);
				}

				//新闻类型设置
				stmt = con.createStatement();
				rs = stmt.executeQuery("select `id`,`name` from news_type");
				String defaultCodes = "";
				String defaultNames = "";
				while (rs.next()) {
					String id = rs.getString("id");
					String name = rs.getString("name");
					// 初始化加载所有的用户信息进入内存中
					newsCategroy.put(id, name);
					categroyNews.put(name, id);
					defaultCodes = defaultCodes + id + ",";
					defaultNames = defaultNames + name + ",";
				}
				defaultNewsType.put("codes",defaultCodes.substring(0,defaultCodes.length()-1));
				defaultNewsType.put("names",defaultNames.substring(0,defaultNames.length()-1));

				//广告
				stmt = con.createStatement();
				rs = stmt.executeQuery("select `id`,`title`,news_image,activity_url,`position`,`description`,update_stmp from advertisement " +
						"where delete_flag = 0 and `status` =1 and start_time<=sysdate() and end_time>=sysdate() order by update_stmp desc");
				int i = 0,j=0;
				adv.clear();
				while (rs.next()) {
					String id = rs.getString("id");
					String title = rs.getString("title");
					String news_image = rs.getString("news_image");
					String activity_url = rs.getString("activity_url");
					String position = rs.getString("position");
					String description = rs.getString("description");
					String update_stmp = rs.getString("update_stmp");
					if(position==null||position.length()==0||"null".equalsIgnoreCase(position)) continue;
					JSONObject object1 = new JSONObject();
					object1.put("advId",id);
					object1.put("advImg",news_image);
					object1.put("advUrl",activity_url);
					object1.put("advTitle",title);
					object1.put("advTime",update_stmp==null?"":update_stmp.substring(0,19));
					object1.put("advDesc",description);
					if(position.contains("1")){
						object1.put("newsType", "广告");
						adv.put("list:"+(i+1),object1);
						i++;
					}

					if(position.contains("2")){
						adv.put("detail:"+(j+1),object1);
						j++;
					}
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
		}
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
	 

		 

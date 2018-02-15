package com.qbao.search.conf;

/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	LoadValues.java<br>
 *
 * Author:      MENG Wenyuan (wymeng@ctrip.com)<br>
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public class LoadValues {

	public static final String FILE_PATH = "conf/qbao/";
	
	public static final String FILE_MAIN  = "main.properties";
	
	public static final String FILE_LOG4J = "log4j.properties";
	public static final String FILE_SUB = "subordinate.properties";
	
	public static final String SEARCHDB_USERNAME = "db.ms.user";
	
	public static final String SEARCHDB_PASSWORD = "db.ms.pass";
	
	public static final String CONFIG_CONNECTION  = "db.ms.connection";

	public static final String USER_LABEL_DICT_SQL  = "recommend.sql.query.select";
	public static final String AREA_SQL  = "area.sql.query.select";
	public static final String CITY_SQL  = "city.sql.query.select";
	public static final String USER_DICT_PARENT  = "user.dict.parent.name";
	public static final String LABEL_INDEX  = "userlabel";
	public static final String LABEL_TYPE  = "user_label";
	public static final String LBS_INDEX  = "lbs";
	public static final String GROUP_INDEX  = "usergroups";
	public static final String USERIDS_INDEX  = "userids";
	public static final String LOG_INDEX  = "log";

	//用户画像默认页大小
	public static final Integer LABEL_SIZE  = 6;


	public static final String SQL_QUERY  = "SELECT id,appid,secret FROM users WHERE openapi_status=1 ORDER BY id";
	public static final String SQL_PROPERTIES_QUERY  = "SELECT property_name,property_value,remark FROM property_table";
	public static final String SQL_PROPERTIES_BY_PARENT  = "SELECT property_name,property_value,remark FROM property_table WHERE topic_name =?";
	
	// 数据导入常量参数
	public static final String LABEL_LBS  = "lbs";	
	public static final String LABEL_TOUTIAO  = "toutiao";
	public static final String LABEL_QBAOLOG  = "qbaolog";
	public static final String LABEL_TASKRECORD  = "taskrecord";
	public static final Integer LABEL_DATA_SIZE  = 10;
	public static final String KAFKA_TOPICS_PRE  = "oneday_";
	public static final String KAFKA_TOPICS_TOUTIAO  = "oneday_toutiao";	
	public static final String KAFKA_TOPICS_QBAOLOG  = "oneday_qbaolog";
	public static final String LBS_ES_FILTER_QUERY  = "poi_id,x_b,y_b,x_g,y_g,x_b_1,y_b_1,x_g_1,y_g_1,shen_code,shen,shi_code,shi,qu_code,qu,poi_name,road_name,poi_type,poi_type_tag,update";
	public static final String TOUTIAO_ES_FILTER_QUERY  = "abstract_content_fenci,image_url, news_url,abstract_content,news_time,title,news_id,hot_time,news_origin,news_type,news_website,news_tags,crawl_date_time,news_tags_fenci";

	



	
}

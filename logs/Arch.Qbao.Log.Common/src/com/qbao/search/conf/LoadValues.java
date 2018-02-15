package com.qbao.search.conf;

import com.qbao.search.util.IndexName;

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
	
	public static final String FILE_LOG4J = "log4j.properties";

	public static final String SEARCHDB_USERNAME = "db.ms.user";
	
	public static final String SEARCHDB_PASSWORD = "db.ms.pass";
	
	public static final String CONFIG_CONNECTION  = "db.ms.connection";

	//public static final String USER_LABEL_DICT_SQL  = "recommend.sql.query.select";
	//public static final String AREA_SQL  = "area.sql.query.select";
	//public static final String CITY_SQL  = "city.sql.query.select";
	//public static final String NEWS_SQL  = "reco.sql";
	//public static final String NEWS_TYPE_SQL  = "type.sql";
	//public static final String NEWS_TYPE_DEFAULT  = "default.newType";
	//public static final String NEWS_TUIJIAN_DEFAULT  = "default.tuijian";
	//public static final String USER_DICT_PARENT  = "user.dict.parent.name";
	public static final String LABEL_INDEX  = "userlabel";
	public static final String LABEL_TYPE  = "user_label";
	public static final String GROUP_INDEX  = "usergroups";
	public static final String USERIDS_INDEX  = "userids";
	public static final String LOG_INDEX  = "log";
	public static final String SPIDER_INDEX  = "toutiao";
	public static final String THUMB_INDEX  = "thumb";
	public static final String QBAO_LOG_INDEX  = "qbaolog";
	public static final String POI_INDEX  = "poi";
	//用户画像默认页大小
	public static final Integer LABEL_SIZE  = 6;


	public static final String SQL_QUERY  = "SELECT id,appid,secret FROM users WHERE openapi_status=1 ORDER BY id";
	public static final String SQL_PROPERTIES_QUERY  = "SELECT property_name,property_value,remark FROM property_table";
	public static final String SQL_PROPERTIES_BY_PARENT  = "SELECT property_name,property_value,remark FROM property_table WHERE topic_name =?";
}

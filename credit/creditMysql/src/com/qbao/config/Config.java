package com.qbao.config;

/**
 * @author song.j
 * @create 2017-09-13 14:14:35
 **/
public class Config {

    public static final String MYBATIS_XML_CONFIG = "resources/MybatisSqlConfig.xml";

    public static final String JDBC_PROPERTIES_DIR = System.getProperty("user.dir") + "/conf/qbao/loadConfig.properties";

    public static final String DEFAULT_DATASOURCE = DataSourceEnvironment.ONEDAY.getDatasource();
}

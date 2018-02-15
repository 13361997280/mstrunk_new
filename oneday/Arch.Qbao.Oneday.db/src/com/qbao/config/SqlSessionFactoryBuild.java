package com.qbao.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author song.j
 * @create 2017-09-13 14:14:26
 **/
public class SqlSessionFactoryBuild {


    private static final Map<String, SqlSessionFactory> sessionFactory = new HashMap();

//    private static SqlSessionFactoryBuild sqlSessionFactoryBuild;
//
//
//    public static SqlSessionFactoryBuild getInstance() {
//
//        if (sqlSessionFactoryBuild == null) {
//            synchronized (SqlSessionFactoryBuild.class) {
//
//                sqlSessionFactoryBuild = new SqlSessionFactoryBuild();
//
//            }
//        }
//
//        return sqlSessionFactoryBuild;
//    }
//
//    private SqlSessionFactoryBuild() {
//
//    }

    /**
     * 获取sqlsessionfactory
     *
     * @param datasourceEnvironment 数据源环境
     * @return
     */
    public static SqlSessionFactory getSqlSessionFactory(DataSourceEnvironment datasourceEnvironment) {

        String datasource;

        if (datasourceEnvironment == null){
            datasource = Config.DEFAULT_DATASOURCE;
        }else {
            datasource = datasourceEnvironment.getDatasource();
        }

        SqlSessionFactory sqlSessionFactory = sessionFactory.get(datasource);

        if (sqlSessionFactory != null) {
            return sqlSessionFactory;
        }

        if (datasourceEnvironment != null)
            sqlSessionFactory = loadSessionFactory(datasource);
        else
            sqlSessionFactory = loadSessionFactory();

        sessionFactory.put(datasource,sqlSessionFactory);

        return sqlSessionFactory;
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return getSqlSessionFactory(null);
    }


    public static SqlSessionFactory loadSessionFactory() {
        return loadSessionFactory(null);
    }

    /**
     * 根据数据库载 sqlsessionfactory
     *
     * @param datasource 数据源ID
     * @return
     */
    public static SqlSessionFactory loadSessionFactory(String datasource) {
        Reader reader = null;
        Properties properties;
        FileInputStream in = null;
        properties = new Properties();
        try {
            reader = Resources.getResourceAsReader(Config.MYBATIS_XML_CONFIG);
            in = new FileInputStream(Config.JDBC_PROPERTIES_DIR);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, datasource, properties);

        return sqlSessionFactory;
    }

}

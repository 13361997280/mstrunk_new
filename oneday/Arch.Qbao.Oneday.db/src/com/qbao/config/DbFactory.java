package com.qbao.config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author song.j
 * @create 2017-09-12 16:16:06
 **/
public class DbFactory {


    /**
     * 使用sqlsessiontemplate 自动close sqlsesion
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T createBean(Class<T> tClass) {
        return createBean(tClass, null);
    }

    /**
     * 用sqlsessiontemplate更安全
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T createBean(Class<T> tClass, DataSourceEnvironment dataSourceEnvironment) {

        return getSqlSessionTemplate(getSqlSessionFactory(dataSourceEnvironment)).getMapper(tClass);
    }

    /**
     * 调用此方法一定要注意。sqlsession 要 close
     * <p>
     * 查询性能更快
     *
     * @return
     */
    public static SqlSession getSqlSession() {
        return getSqlSessionFactory(null).openSession(true);
    }

    private static SqlSessionTemplate getSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    private static SqlSessionFactory getSqlSessionFactory(DataSourceEnvironment dataSourceEnvironment) {
        return SqlSessionFactoryBuild.getSqlSessionFactory(dataSourceEnvironment);
    }

}

package com.qbao.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author song.j
 * @create 2017-10-27 10:10:16
 **/
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public synchronized DataSource getDataSource() {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(this.props.getProperty("driver"));
        druidDataSource.setUrl(this.props.getProperty("url"));
        druidDataSource.setUsername(this.props.getProperty("username"));
        druidDataSource.setPassword(this.props.getProperty("password"));
        druidDataSource.setMinIdle(Integer.valueOf(this.props.getProperty("minIdle")));
        druidDataSource.setMaxActive(Integer.valueOf(this.props.getProperty("maxActive")));
        druidDataSource.setInitialSize(Integer.valueOf(this.props.getProperty("initialSize")));
        druidDataSource.setMinEvictableIdleTimeMillis(Long.valueOf(this.props.getProperty("minEvictableIdleTimeMillis")));
        druidDataSource.setValidationQuery(this.props.getProperty("validationQuery"));
        druidDataSource.setTestWhileIdle(Boolean.valueOf(this.props.getProperty("testWhileIdle")));
        druidDataSource.setTestOnBorrow(Boolean.valueOf(this.props.getProperty("testOnBorrow")));
        druidDataSource.setTestOnReturn(Boolean.valueOf(this.props.getProperty("testOnReturn")));
        // 其他配置可以根据MyBatis主配置文件进行配置
        try
        {
            druidDataSource.init();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return druidDataSource;
    }
}

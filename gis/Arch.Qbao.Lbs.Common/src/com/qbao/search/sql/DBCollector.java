package com.qbao.search.sql;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.CommonUtil;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2012-5-2 14:31:39
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2012-5-2 14:31:39  	li_yao		1.0			Newly created
 */
public class DBCollector<E> implements Iterator<E>, Closeable {
	private static final ESLogger logger = Loggers.getLogger(DBCollector.class);
	private Connection connection;
	private Statement statement;
	private ResultSet rs;
	
	int maxSkipped;
	List<String> skippedElements;
	
	private ElementWrapper<E> wrapper;

	public void startCollect(Connection connection, String sql,
			ElementWrapper<E> wrapper) throws Exception {
		this.connection = connection;
		this.wrapper = wrapper;
		maxSkipped = Config.get().getInt("collector.max.skipped", 200);
		skippedElements = new ArrayList<String>(maxSkipped);
		statement = connection.createStatement();
		statement.setQueryTimeout(Config.get().getInt(
				"collector.sql.timeout.seconds", 30 * 60));
		statement.setFetchSize(Config.get().getInt(
				"collector.batch.rows.num", 10000));
		logger.info("collect with sql:{}", sql);
		rs = statement.executeQuery(sql);
	}

	
	@Override
	public boolean hasNext() {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public E next() {
		E element = null;
		try {
			element = wrapper.wrap(rs);
		} catch(Exception e) {
			if(e instanceof SQLException) {
				throw new RuntimeException(e);
			}
			try {
				StringBuilder sb = new StringBuilder(1000);
				int columnCount = rs.getMetaData().getColumnCount();
				sb.append("ResultSet:[");
				for(int i = 0; i < columnCount; i++) {
					String columnName = rs.getMetaData().getColumnLabel(i + 1);
					String columnValue = rs.getString(columnName);
					sb.append(columnName);
					sb.append(":'");
					sb.append(columnValue);
					sb.append("',");
				}
				if(columnCount > 0) {
					sb.setLength(sb.length() - 1);//delete the last ,
				}
				sb.append("]\nexception:");
				sb.append(CommonUtil.toString(e));
				skippedElements.add(sb.toString());
			} catch(Exception e2) {
				throw new RuntimeException(e2);
			}
			if(skippedElements.size() > maxSkipped) {
				throw new RuntimeException("too many error elements:" +
						skippedElements.size() + " "+skippedElements);
			}
		}
		
		return element;
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException(
			getClass().getSimpleName() + " don't support remove operation!");
	}


	@Override
	public void close() throws IOException {
		if(skippedElements.size() > 0) {
			StringBuilder sb = new StringBuilder(skippedElements.size() * 50);
			sb.append("skipped elements num:");
			sb.append(skippedElements.size());
			sb.append('\n');
			for(String s:skippedElements) {
				sb.append(s);
				sb.append('\n');
			}
			logger.error(sb.toString());
		}
		

		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				logger.warn(e);
			}
		}
		
		if(statement != null){
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warn(e);
			}
		}
		
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				logger.warn(e);
			}
		}
		
	}
}

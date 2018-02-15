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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class DBCollector20130916<E> implements Iterator<E>, Closeable {
	private static final ESLogger logger = 
			Loggers.getLogger(DBCollector20130916.class);
	
	public static final String INIT_TIMESTAMP = "1970-01-01 00:00:00.000";

	private Connection connection;
	String sql;
	String[] batchFields;
	String[] lastMaxBatchVals;
	int batchNum;
	
	ThreadPoolExecutor executor;
	BlockingQueue<E> queue;
	volatile boolean end = false;
	volatile private int rsCount = 0;
	int taskCount = 0;
	Exception collecException = null;
	int timeOutMills = 5 * 60 * 1000;
	
	
	int maxSkipped;
	List<String> skippedElements;
	
	private ElementWrapper<E> wrapper;

	public void startCollect(Connection connection, String sql,
			String[] batchFields, int batchNum, ElementWrapper<E> wrapper)
				throws Exception {
		this.connection = connection;
		this.sql = sql;
		if(batchFields != null && batchFields.length > 0 && batchNum > 0) {
			//should batch
			this.batchNum = batchNum;
			this.batchFields = batchFields;
			this.lastMaxBatchVals = new String[batchFields.length];
			this.queue = new ArrayBlockingQueue<E>(batchNum, true);
		} else {
			this.batchNum = Integer.MAX_VALUE;
			this.batchFields = null;//not batch
			this.queue = new LinkedBlockingQueue<E>();
		}
		
		this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
		
		this.wrapper = wrapper;
		
		this.maxSkipped = Config.get().getInt("collector.max.skipped", 200);
		this.skippedElements = new ArrayList<String>(maxSkipped);
		
		this.timeOutMills = Config.get().getInt("collector.timeout.seconds",
				300) * 1000;
	}

	String getCondition(int fieldIndex) {
		String sql;
		if(lastMaxBatchVals[fieldIndex] == null) {
			sql = " case when " + batchFields[fieldIndex] + " is not null then 1 else";
			if(fieldIndex < batchFields.length - 1) {//not the last
				sql += getCondition(++fieldIndex);
			} else {
				sql += " 0";
			}
			sql += " end";
		} else {
			sql = " case when " + batchFields[fieldIndex] + ">'" + 
					lastMaxBatchVals[fieldIndex] + "' then 1";
			if(fieldIndex < batchFields.length - 1) {//not the last
				sql += " when " + batchFields[fieldIndex] + "='" + 
					lastMaxBatchVals[fieldIndex] + "' then" +
					getCondition(++fieldIndex);
			}
			sql += " else 0 end";
		}
		return sql;
	}

	protected synchronized void collect()
			throws SQLException, InterruptedException {
		if(end) {
			return;
		}
		Statement statement = null;
		ResultSet rs = null;
		try{
			statement = connection.createStatement();
			String batchSql;
			if(batchFields == null) {
				batchSql = sql;
			} else {
				batchSql = "select top " + batchNum + " * from (" + sql + 
						") as t";
				if(rsCount > 0) {//not the first batch
					batchSql += " where" + getCondition(0) + "=1";
				}
				batchSql += " order by";
				for(int i = 0; i < batchFields.length; i++) {
					batchSql += (i == 0 ? " " : ",") + batchFields[i] + " asc";
				}
			}

			logger.info("collect with batch sql:{}", batchSql);
			statement.setQueryTimeout(Config.get().getInt(
				"collect.batch.sql.timeout.seconds", 30 * 60));
			logger.info("fecth size:" + statement.getFetchSize());
			rs = statement.executeQuery(batchSql);
			int batchCount = 0;
			while(rs.next()){
				++batchCount;
				E doc = null;
				try {
					doc = wrapper.wrap(rs);
				} catch(Exception e) {
					logger.error("wrap doc error:", e);
					try {
						StringBuilder sb = new StringBuilder(1000);
						int columnCount = rs.getMetaData().getColumnCount();
						sb.append("ResultSet:[");
						for(int i = 0; i < columnCount; i++) {
							String columnName = rs.getMetaData().getColumnLabel(i + 1);
							String columnValue = rs.getString(columnName);
							if(columnValue != null && columnValue.length() > 40) {
								columnValue = columnValue.substring(0, 40) + "...";
							}
							sb.append(columnName);
							sb.append(":'");
							sb.append(columnValue);
							sb.append("',");
						}
						if(columnCount > 0) {
							sb.setLength(sb.length() - 1);//delete the last ,
						}
						sb.append("]#exception:");
						sb.append(CommonUtil.toString(e));
						skippedElements.add(sb.toString());
					} catch(Exception e2) {
						logger.error("Error while add skipped element", e2);
					}
					if(skippedElements.size() > maxSkipped) {
						throw new RuntimeException("too many error elements:" +
								skippedElements.size());
					}
				}
				
				
				if(doc != null) {
					queue.put(doc);
				}
				if(batchCount == batchNum) {//the last of current batch
					for(int i = 0; i < batchFields.length; i++) {
						lastMaxBatchVals[i] = rs.getString(batchFields[i]);
					}
				}
			}
			rsCount += batchCount;
			logger.info("{} records selected...", rsCount);
			if(batchCount < batchNum) {
				logger.info("batchCount:{} < batchNum:{}", batchCount, batchNum);
				logger.info("{} records selected totally!!!", rsCount);
				end = true;
			}
		} finally {
			try{
				if(rs != null){
					rs.close();
				}
			} finally {
				if(statement != null){
					statement.close();
				}
			}
		}
	}
	
	void startTask() {
		if(!end && queue.size() <= batchNum/2 && executor.getQueue().size() < 2) {
			logger.info("start " + ++taskCount + " task");
			executor.submit(new Runnable(){
				@Override
				public void run() {
					try {
						collect();
					} catch (Exception e) {
						collecException = e;
						end = true;
						logger.error(e);
					}
				}
				
			});
		}
	}

	
	@Override
	public boolean hasNext() {
		final int waitInterval = 10;
		int waitedTime = 0;
		startTask();
		while(queue.peek() == null) {
			if(end && queue.peek() == null) {
				if(collecException != null) {
					throw new RuntimeException(collecException);
				}
				return false;
			}			
			if(waitedTime > timeOutMills) {
				throw new RuntimeException(
					"expired " + timeOutMills/1000 + " seconds");
			}
			startTask();
			try {
				Thread.sleep(waitInterval);
				waitedTime += waitInterval;
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		return true;
	}


	@Override
	public E next() {
		return queue.poll();
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
		try {
			executor.shutdown();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new IOException(e);
				}
			}
		}
	}
}

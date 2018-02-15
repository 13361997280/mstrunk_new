package com.qbao.search.sql;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public abstract class DBCollector20130627<E> implements Iterator<E>, Closeable {
	private static final ESLogger logger = 
			Loggers.getLogger(DBCollector20130627.class);
	
	public static final String INIT_TIMESTAMP = "1970-01-01 00:00:00.000";

	
	protected Connection connection;
	ThreadPoolExecutor executor;
	int batch;
	BlockingQueue<E> queue;
	volatile boolean end;
	String tableName;
	String timeField;
	String selectFields;
	String whereCondition;
	
	private int rsCount = 0;
	
	int taskCount = 0;
	
	Exception collecException = null;
	
	int maxLoop = 2 *60 * 1000;
	
	private long startTimestamp;
	private long maxTimestamp;
	private long stepTimestamp;

	
	public DBCollector20130627() throws SQLException, IOException {
		connection = DBUtil.getConnection();
	}
	

	public void startCollect(int batch, String tableName, String timeField,
			String selectFields, String whereCondition) throws Exception {
		executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
		this.batch = batch;
		this.queue = new ArrayBlockingQueue<E>(batch, true);
		this.end = false;
		this.tableName = tableName;
		this.timeField = timeField;
		this.selectFields = selectFields;
		
		if(whereCondition == null) {
			whereCondition = "";
		} else {
			whereCondition = whereCondition.trim();
			if(!whereCondition.isEmpty() && 
					!whereCondition.matches("^(?i)where +.*")) {
				whereCondition = "where " + whereCondition;
			}
		}
		this.whereCondition = whereCondition;
		
		checkShouldBatch();
	}
	
	protected void initTimestampRange(int total, int batch, String minTimestamp,
			String maxTimestamp) {
		try {
			this.startTimestamp = CommonUtil.getTime(minTimestamp);
			this.maxTimestamp = CommonUtil.getTime(maxTimestamp);
			this.stepTimestamp = Math.max(10, (this.maxTimestamp - 
				this.startTimestamp) / Math.max(1, total/batch));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String[] nextTimestampRange() {
		if(startTimestamp - maxTimestamp > 10) {
			return null;
		}
		return new String[]{
			CommonUtil.formatTime(startTimestamp),
			CommonUtil.formatTime(startTimestamp += stepTimestamp)
		};
	}

	
	protected long processTimestamp(ResultSet rs, String timeField)
			throws SQLException {
		String timestamp = rs.getString(timeField);
		if(timestamp != null) {
			try {
				return CommonUtil.getTime(timestamp);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return 0;
	}	

	
	protected abstract E genElement(ResultSet rs, long timestamp)
		throws SQLException;
	
	void checkShouldBatch() throws SQLException {
		String sql = "select COUNT(0),MIN(" + timeField + "),MAX(" + timeField
			+ ") from " + tableName + " " + whereCondition;
		logger.info("count sql:{}", sql);
		String[] strs = DBUtil.get(connection, sql);
		int total = Integer.parseInt(strs[0]);
		if(total < 1) {
			end = true;
		}
		else {
			whereCondition += (whereCondition.isEmpty() ? "where " : " and ");
			initTimestampRange(total, batch, strs[1], strs[2]);
		}
	
		logger.info("{} docs counted!!!", total);
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
			String[] timeRange = nextTimestampRange();
			if(timeRange == null) {
				//System.out.println(rsCount + " records selected!!!");
				logger.info("{} records selected!!!", rsCount);
				end = true;
				return;
			}
			String sql = "select " + selectFields + " from " +
				tableName + " " + whereCondition + timeField + ">='" +
				timeRange[0]+ "' and " + timeField + "<'" + timeRange[1] + "'";

			logger.info("collect with sql:{}", sql);
			rs = statement.executeQuery(sql);
			while(rs.next()){
				E doc = genElement(rs, processTimestamp(rs, timeField));
				if(doc != null) {
					queue.put(doc);
				}
				++rsCount;
				if(rsCount % 5000 == 0) {
					//System.out.println(rsCount + " records selected...");
					logger.info("{} records selected...", rsCount);
				}
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
		if(!end && queue.size() <= batch/2 && executor.getQueue().size() < 2) {
			logger.info("start " + ++taskCount + " task");
			executor.submit(new Runnable(){
				@Override
				public void run() {
					try {
						collect();
					} catch (Exception e) {
						collecException = e;
						logger.error(e);
					}
				}
				
			});
		}
	}

	
	@Override
	public boolean hasNext() {
		int loop = 0;
		while(queue.peek() == null) {
			if(collecException != null) {
				throw new RuntimeException(collecException);
			}
			if(end && queue.peek() == null) {
				return false;
			}
			startTask();
			if(++loop > maxLoop) {
				throw new RuntimeException(
					"expired " + maxLoop/1000 + " seconds");
			}
			try {
				Thread.sleep(1);
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

	public int collectCount() {
		return rsCount;
	}

	@Override
	public void close() throws IOException {
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				throw new IOException(e);
			}
		}
		
	}
}

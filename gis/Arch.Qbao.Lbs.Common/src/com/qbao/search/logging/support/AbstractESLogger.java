/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.qbao.search.logging.support;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.util.SlideWindowCounter;

/**
 * @author kimchy (shay.banon)
 */
public abstract class AbstractESLogger implements ESLogger {
	
	final public static ThreadPoolExecutor executor = 
		new ThreadPoolExecutor( 
			Config.get().getInt("log4j.executor.thread.num", 2), 
			Config.get().getInt("log4j.executor.thread.num", 2),
			0L, 
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(
				Config.get().getInt("log4j.executor.queue.num", 5000)),
			new ThreadPoolExecutor.DiscardPolicy()
		);
		//Executors.newSingleThreadExecutor();
	
	final public static SlideWindowCounter WARN_COUNTER = 
			SlideWindowCounter.get(5 * 60, 5);
	
	final public static SlideWindowCounter ERROR_COUNTER = 
			SlideWindowCounter.get(5 * 60, 5);

	private final String prefix;

	protected AbstractESLogger(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}
	
	static class TraceMsgParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Object[] params;
		TraceMsgParams(AbstractESLogger logger, String msg, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.params = params;
		}
		
		public void run(){
			logger.internalTrace(LoggerMessageFormat.format(logger.prefix, msg,
				params));
		}
	}

	public void trace(final String msg, final Object... params) {
		if (isTraceEnabled()) {
			submitLogTask(new TraceMsgParams(this, msg, params));
		}
	}

	protected abstract void internalTrace(String msg);
	
	static class TraceMsgCauseParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Throwable cause;
		Object[] params;
		TraceMsgCauseParams(AbstractESLogger logger, String msg, 
				Throwable cause, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.cause = cause;
			this.params = params;
		}
		
		public void run(){
			logger.internalTrace(LoggerMessageFormat.format(logger.prefix, msg,
				params), cause);
		}
	}

	public void trace(final String msg, final Throwable cause,
			final Object... params) {
		if (isTraceEnabled()) {
			submitLogTask(new TraceMsgCauseParams(this, msg, cause, params));
		}
	}

	protected abstract void internalTrace(String msg, Throwable cause);
	

	public void trace(Throwable cause){
		trace("", cause);
	}

	static class DebugMsgParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Object[] params;
		DebugMsgParams(AbstractESLogger logger, String msg, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.params = params;
		}
		
		public void run(){
			logger.internalDebug(LoggerMessageFormat.format(logger.prefix, msg,
					params));
		}
	}
	
	public void debug(final String msg, final Object... params) {
		if (isDebugEnabled()) {
			submitLogTask(new DebugMsgParams(this, msg, params));
		}
	}

	protected abstract void internalDebug(String msg);
	
	static class DebugMsgCauseParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Throwable cause;
		Object[] params;
		DebugMsgCauseParams(AbstractESLogger logger, String msg, 
				Throwable cause, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.cause = cause;
			this.params = params;
		}
		
		public void run(){
			logger.internalDebug(LoggerMessageFormat.format(logger.prefix, msg,
				params), cause);
		}
	}

	public void debug(final String msg, final Throwable cause,
			final Object... params) {
		if (isDebugEnabled()) {
			submitLogTask(new DebugMsgCauseParams(this, msg, cause, params));
		}
	}

	protected abstract void internalDebug(String msg, Throwable cause);

	public void debug(Throwable cause){
		debug("", cause);
	}
	
	
	
	
	static class SearchMsgParams implements Runnable{
		AbstractESLogger logger;
		Map<String,String> msg;
		 
		SearchMsgParams(AbstractESLogger logger,Map<String,String> map) {
			this.logger = logger;
			this.msg = map; 
		}		
		public void run(){
			logger.internalInfo(msg);
		}
	}
	
	protected abstract void internalInfo(Map<String,String> msg);
	
	public void searchLog(Map<String,String> map){
		submitLogTask(new SearchMsgParams(this,map));		
	}
	
	
	static class InfoMsgParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Object[] params;
		InfoMsgParams(AbstractESLogger logger, String msg, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.params = params;
		}
		
		public void run(){
			logger.internalInfo(LoggerMessageFormat.format(logger.prefix, msg,
				params));
		}
	}	
	
	public void info(final String msg, final Object... params) {
		if (isInfoEnabled()) {
			submitLogTask(new InfoMsgParams(this, msg, params));
		}
	}

	protected abstract void internalInfo(String msg);
	
	static class InfoMsgCauseParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Throwable cause;
		Object[] params;
		InfoMsgCauseParams(AbstractESLogger logger, String msg, 
				Throwable cause, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.cause = cause;
			this.params = params;
		}
		
		public void run(){
			logger.internalInfo(LoggerMessageFormat.format(logger.prefix, msg,
					params), cause);
		}
	}

	public void info(final String msg, final Throwable cause,
			final Object... params) {
		if (isInfoEnabled()) {
			submitLogTask(new InfoMsgCauseParams(this, msg, cause, params));
		}
	}

	protected abstract void internalInfo(String msg, Throwable cause);
	
	public void info(Throwable cause){
		info("", cause);
	}
	
	static class WarnMsgParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Object[] params;
		WarnMsgParams(AbstractESLogger logger, String msg, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.params = params;
		}
		
		public void run(){
			logger.internalWarn(LoggerMessageFormat.format(logger.prefix, msg,
					params));
		}
	}	

	public void warn(final String msg, final Object... params) {
		WARN_COUNTER.setCount();
		if (isWarnEnabled()) {
			submitLogTask(new WarnMsgParams(this, msg, params));
		}
	}

	protected abstract void internalWarn(String msg);
	
	static class WarnMsgCauseParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Throwable cause;
		Object[] params;
		WarnMsgCauseParams(AbstractESLogger logger, String msg, 
				Throwable cause, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.cause = cause;
			this.params = params;
		}
		
		public void run(){
			logger.internalWarn(LoggerMessageFormat.format(logger.prefix, msg,
					params), cause);
		}
	}	

	public void warn(final String msg, final Throwable cause,
			final Object... params) {
		WARN_COUNTER.setCount();
		if (isWarnEnabled()) {
			submitLogTask(new WarnMsgCauseParams(this, msg, cause, params));
		}
	}

	protected abstract void internalWarn(String msg, Throwable cause);
	
	public void warn(Throwable cause){
		warn("", cause);
	}
	
	static class ErrorMsgParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Object[] params;
		ErrorMsgParams(AbstractESLogger logger, String msg, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.params = params;
		}

		@Override
		public void run() {
			logger.internalError(LoggerMessageFormat.format(logger.prefix, msg,
				params));
		}
	}

	public void error(final String msg, final Object... params) {
		ERROR_COUNTER.setCount();
		if (isErrorEnabled()) {
			submitLogTask(new ErrorMsgParams(this, msg, params));
		}
	}

	protected abstract void internalError(String msg);
	
	static class ErrorMsgCauseParams implements Runnable{
		AbstractESLogger logger;
		String msg;
		Throwable cause;
		Object[] params;
		ErrorMsgCauseParams(AbstractESLogger logger, String msg, 
				Throwable cause, Object... params) {
			this.logger = logger;
			this.msg = msg;
			this.cause = cause;
			this.params = params;
		}
		
		public void run(){
			logger.internalError(LoggerMessageFormat.format(logger.prefix, msg,
				params), cause);
		}
	}

	public void error(final String msg, final Throwable cause,
			final Object... params) {
		ERROR_COUNTER.setCount();
		if (isErrorEnabled()) {
			submitLogTask(new ErrorMsgCauseParams(this, msg, cause, params));
		}
	}

	protected abstract void internalError(String msg, Throwable cause);
	
	public void error(final Throwable cause){
		error("", cause);		
	}
	
	void submitLogTask(Runnable task) {
		try {
			executor.submit(task);
		} catch(Exception e) {
			//supress it,RejectedExecutionException
		}
	}
}

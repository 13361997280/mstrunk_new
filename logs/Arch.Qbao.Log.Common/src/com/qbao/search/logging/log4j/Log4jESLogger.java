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

package com.qbao.search.logging.log4j;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import com.qbao.search.logging.support.AbstractESLogger;
import com.qbao.search.util.CommonUtil;

/**
 * @author kimchy (shay.banon)
 */
public class Log4jESLogger extends AbstractESLogger {

	private final Logger logger;

	public Log4jESLogger(String prefix, Logger logger) {
		super(prefix);
		this.logger = logger;

	}

	public String getName() {
		return logger.getName();
	}

	public boolean isTraceEnabled() {
		return true;
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Level.WARN);
	}

	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Level.ERROR);
	}

	protected void internalTrace(String msg) {
		//logger.trace(msg);
	}

	protected void internalTrace(String msg, Throwable cause) {
		//logger.trace(msg, cause);
	}

	protected void internalDebug(String msg) {
		logger.debug(msg);
	}

	protected void internalDebug(String msg, Throwable cause) {
		logger.debug(msg, cause);
	}

	protected void internalInfo(String msg) {
		logger.info(msg);
	}

	protected void internalInfo(String msg, Throwable cause) {
		logger.info(msg, cause);
	}
	
	private static class SearchLogLevel extends Level {
		private static final long serialVersionUID = 1076913470822079835L;

		private SearchLogLevel(int level, String name, int sysLogLevel) {
			super(level, name, sysLogLevel);
		}
	}

	private static final Level SEARCH_LOG_LEVEL = new SearchLogLevel(30050,
			"SEARCHLOG", SyslogAppender.LOG_LOCAL0);
	
	
	protected void internalInfo(Map<String,String> map) {
		logger.log(SEARCH_LOG_LEVEL,map);
	}
	
	protected void internalWarn(String msg) {
		logger.warn(msg);
	}

	protected void internalWarn(String msg, Throwable cause) {
		logger.warn(msg, cause);
	}

	protected void internalError(String msg) {
		logger.error(msg);
		try {
			CommonUtil.sendMail(CommonUtil.SEREVER_NAME, msg);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		} catch (MessagingException e) {
			logger.error(e);
		}
	}

	protected void internalError(String msg, Throwable cause) {
		logger.error(msg, cause);
		try {
			CommonUtil.sendMail(CommonUtil.SEREVER_NAME,
				msg + "\nexception:" + CommonUtil.toString(cause));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		} catch (MessagingException e) {
			logger.error(e);
		}
	}


}

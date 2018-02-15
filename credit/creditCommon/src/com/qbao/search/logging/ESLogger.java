package com.qbao.search.logging;

import java.util.Map;

/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	ESLogger.java<br>
 *
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public interface ESLogger {

	String getPrefix();

	String getName();

	/**
	 * Returns {@code true} if a TRACE level message is logged.
	 */
	boolean isTraceEnabled();

	/**
	 * Returns {@code true} if a DEBUG level message is logged.
	 */
	boolean isDebugEnabled();

	/**
	 * Returns {@code true} if an INFO level message is logged.
	 */
	boolean isInfoEnabled();

	/**
	 * Returns {@code true} if a WARN level message is logged.
	 */
	boolean isWarnEnabled();

	/**
	 * Returns {@code true} if an ERROR level message is logged.
	 */
	boolean isErrorEnabled();

	/**
	 * Logs a DEBUG level message.
	 */
	void trace(String msg, Object... params);

	/**
	 * Logs a DEBUG level message.
	 */
	void trace(String msg, Throwable cause, Object... params);
	
	/**
	 * Logs a DEBUG throwable
	 * @param cause
	 */
	void trace(Throwable cause);

	/**
	 * Logs a DEBUG level message.
	 */
	void debug(String msg, Object... params);

	/**
	 * Logs a DEBUG level message.
	 */
	void debug(String msg, Throwable cause, Object... params);
	
	/**
	 * Logs a DEBUG throwable
	 * @param cause
	 */
	void debug(Throwable cause);

	/**
	 * Logs an INFO level message.
	 */
	void info(String msg, Object... params);
	
	void searchLog(Map<String,String> map);
	
	/**
	 * Logs an INFO level message.
	 */
	void info(String msg, Throwable cause, Object... params);
	
	/**
	 * Logs a DEBUG throwable
	 * @param cause
	 */
	void info(Throwable cause); 

	/**
	 * Logs a WARN level message.
	 */
	void warn(String msg, Object... params);

	/**
	 * Logs a WARN level message.
	 */
	void warn(String msg, Throwable cause, Object... params);
	
	/**
	 * Logs a DEBUG throwable
	 * @param cause
	 */
	void warn(Throwable cause);

	/**
	 * Logs an ERROR level message.
	 */
	void error(String msg, Object... params);

	/**
	 * Logs an ERROR level message.
	 */
	void error(String msg, Throwable cause, Object... params);
	
	/**
	 * Logs a DEBUG throwable
	 * @param cause
	 */
	void error(Throwable cause);
	
}

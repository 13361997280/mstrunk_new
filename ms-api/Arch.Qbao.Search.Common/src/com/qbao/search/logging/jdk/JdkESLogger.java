package com.qbao.search.logging.jdk;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.qbao.search.logging.support.AbstractESLogger;

/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	JdkESLogger.java<br>
 *
 * Author:      MENG Wenyuan (wymeng@ctrip.com)<br>
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public class JdkESLogger extends AbstractESLogger {

	private final Logger logger;

	public JdkESLogger(String prefix, Logger logger) {
		super(prefix);
		this.logger = logger;
	}

	public String getName() {
		return logger.getName();
	}

	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINEST);
	}

	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	protected void internalTrace(String msg) {
		logger.log(Level.FINEST, msg);
	}

	protected void internalTrace(String msg, Throwable cause) {
		logger.log(Level.FINEST, msg, cause);
	}

	protected void internalDebug(String msg) {
		logger.log(Level.FINE, msg);
	}

	protected void internalDebug(String msg, Throwable cause) {
		logger.log(Level.FINE, msg, cause);
	}

	protected void internalInfo(String msg) {
		logger.log(Level.INFO, msg);
	}
	
	protected void internalInfos(String title, String msg) {
		logger.log(Level.INFO, title +" " + msg);
	}
	
	
	protected void internalInfo(String msg, Throwable cause) {
		logger.log(Level.INFO, msg, cause);
	}

	protected void internalWarn(String msg) {
		logger.log(Level.WARNING, msg);
	}

	protected void internalWarn(String msg, Throwable cause) {
		logger.log(Level.WARNING, msg, cause);
	}

	protected void internalError(String msg) {
		logger.log(Level.SEVERE, msg);
	}

	protected void internalError(String msg, Throwable cause) {
		logger.log(Level.SEVERE, msg, cause);
	}

	@Override
	protected void internalInfo(Map<String, String> msg) {
		// TODO Auto-generated method stub
		
	}	

}

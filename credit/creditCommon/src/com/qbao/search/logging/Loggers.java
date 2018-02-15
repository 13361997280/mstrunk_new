package com.qbao.search.logging;
/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	Loggers.java<br>
 *
 * Author:      MENG Wenyuan (wymeng@ctrip.com)<br>
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public class Loggers {

	private static boolean consoleLoggingEnabled = true;

	public static void disableConsoleLogging() {
		consoleLoggingEnabled = false;
	}

	public static void enableConsoleLogging() {
		consoleLoggingEnabled = true;
	}

	public static boolean consoleLoggingEnabled() {
		return consoleLoggingEnabled;
	}

	public static ESLogger getLogger(ESLogger parentLogger, String s) {
		return getLogger(parentLogger.getName() + s, parentLogger.getPrefix());
	}

	public static ESLogger getLogger(String s) {
		return ESLoggerFactory.getLogger(s);
	}
	
	public static ESLogger getLogger(Object obj) {
		return ESLoggerFactory.getLogger(getLoggerName(obj.getClass()));
	}

	public static ESLogger getLogger(Class<?> clazz) {
		return ESLoggerFactory.getLogger(getLoggerName(clazz));
	}
	
	public static ESLogger getLogger(Object obj, String... prefixes) {
		return getLogger(obj.getClass(), prefixes);
	}

	public static ESLogger getLogger(Class<?> clazz, String... prefixes) {
		return getLogger(getLoggerName(clazz), prefixes);
	}

	public static ESLogger getLogger(String name, String... prefixes) {
		String prefix = null;
		if (prefixes != null && prefixes.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (String prefixX : prefixes) {
				if (prefixX != null) {
					sb.append("[").append(prefixX).append("]");
				}
			}
			if (sb.length() > 0) {
				sb.append(" ");
				prefix = sb.toString();
			}
		}
		return ESLoggerFactory.getLogger(prefix, name);
	}

	private static String getLoggerName(Class<?> clazz) {
		return clazz.getName();
	}
}

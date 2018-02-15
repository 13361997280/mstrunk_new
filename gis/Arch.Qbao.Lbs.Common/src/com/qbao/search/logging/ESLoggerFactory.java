package com.qbao.search.logging;

import com.qbao.search.conf.LoadConfig;
import com.qbao.search.logging.log4j.Log4jESLoggerFactory;
import com.qbao.search.util.IndexName;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	ESLoggerFactory.java<br>
 *
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public abstract class ESLoggerFactory {

	private static volatile ESLoggerFactory defaultFactory;

	static {
		// try {
		// Class.forName("org.slf4j.Logger");
		// defaultFactory = new Slf4jESLoggerFactory();
		// } catch (Throwable e) {
		// throw new RuntimeException(e);
		// no slf4j
		// }
		
		try{
			//LoadConfig.get().load(false);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			Class.forName("org.apache.log4j.Logger");
			File f = new File("conf/qbao/log4j.properties");
			if (!f.exists()) {
				System.out.println("Cannot find log config file:"
						+ f.getAbsolutePath());
			}
			PropertyConfigurator.configureAndWatch(f.getAbsolutePath());
			defaultFactory = new Log4jESLoggerFactory();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the default factory.
	 */
	public static void setDefaultFactory(ESLoggerFactory defaultFactory) {
		if (defaultFactory == null) {
			throw new NullPointerException("defaultFactory");
		}
		ESLoggerFactory.defaultFactory = defaultFactory;
	}

	public static ESLogger getLogger(String prefix, String name) {
		return defaultFactory.newInstance(prefix, name);
	}

	public static ESLogger getLogger(String name) {
		return defaultFactory.newInstance(name);
	}

	public ESLogger newInstance(String name) {
		return newInstance(null, name);
	}

	public abstract ESLogger newInstance(String prefix, String name);
}

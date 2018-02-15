
package com.qbao.search.logging.jdk;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.ESLoggerFactory;

/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	JdkESLoggerFactory.java<br>
 *
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public class JdkESLoggerFactory extends ESLoggerFactory {

	public ESLogger newInstance(String prefix, String name) {
		final java.util.logging.Logger logger = java.util.logging.Logger
				.getLogger(name);
		return new JdkESLogger(prefix, logger);
	}
}

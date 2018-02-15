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

import org.apache.log4j.Logger;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.ESLoggerFactory;

/**
 * @author kimchy (shay.banon)
 */
public class Log4jESLoggerFactory extends ESLoggerFactory {

	@Override
	public ESLogger newInstance(String prefix, String name) {
		return new Log4jESLogger(prefix, Logger.getLogger(name));
	}
}

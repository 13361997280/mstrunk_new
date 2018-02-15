package com.qbao.search.rpc;

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
 * @Create-at 2011-12-6 14:43:24
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-6 14:43:24  	li_yao		1.0			Newly created
 */
public interface ScheduleServer<E, V> extends Server {
	/**
	 * start the interval schedule
	 */
	V startSchedule(E info);
	/**
	 * stop the interval schedule
	 */
	V stopSchedule(E info);
}

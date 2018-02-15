/**
 * 
 */
package com.qbao.search.util;

/**
 * @author Administrator
 * 
 */
public class TimeUsed {

	private long start;

	public TimeUsed() {
		start();
	}

	public void start() {
		start = System.currentTimeMillis();
	}

	public String used() {
		return "time used:" + useds()+ "!";
	}
	
	public String useds() {
		return (System.currentTimeMillis() - start) / 1000+ "s";
	}

}

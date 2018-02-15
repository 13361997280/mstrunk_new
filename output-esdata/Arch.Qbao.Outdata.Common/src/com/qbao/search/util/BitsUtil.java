package com.qbao.search.util;

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
 * @Create-at 2012-7-6 11:00:59
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2012-7-6 11:00:59  	li_yao		1.0			Newly created
 */
public class BitsUtil {
	
	public static int toZeros(int src, int start, int len) {
		for(int i = start; i < start + len; i++) {
			src &= ~(1 << i);
		}
		return src;
	}
	
	public static int toOnes(int src, int start, int len) {
		for(int i = start; i < start + len; i++) {
			src |= 1 << i;
		}
		return src;
	}
	
	private static final int[][] ZEROS = new int[32][];
	
	private static  final int[][] ONES = new int[32][]; 
	
	static {
		for(int start = 0; start < 32; start++) {
			ZEROS[start] = new int[33];
			for(int len = 1; len < 33 - start; len++) {
				ZEROS[start][len] = toZeros(-1, start, len);
			}
		}
	}
	
	static {
		for(int start = 0; start < 32; start++) {
			ONES[start] = new int[33];
			for(int len = 1; len < 33 - start; len++) {
				ONES[start][len] = toOnes(0, start, len);
			}
		}
	}
	
	public static int getZeros(int start, int len) {
		return ZEROS[start][len];
	}
	
	public static int getOnes(int start, int len) {
		return ONES[start][len];
	}
	
	public static int get(int src, int start, int len) {
		return (src & ONES[start][len]) >>> start;
	}
	
	public static int set(int src, int val, int start, int len) {
		return (src & ZEROS[start][len]) | (val << start);
	}

	public static int compositeInt(int high, int low, int lowBitsNum) {
		if(low >>> lowBitsNum > 0) {
			throw new IllegalArgumentException("low:" + low + 
				" has exceed the up-limit with lowBitsNum:" + lowBitsNum);
		}
		if(high >>> (31 - lowBitsNum) > 0) {
			throw new IllegalArgumentException("high:" + high + 
				" has exceed the up-limit with lowBitsNum:" + lowBitsNum);
		}
		return high << lowBitsNum | low;
	}
	
	public static int compositeInt(String high, String low, int lowBitsNum) {
		return compositeInt(Integer.parseInt(high), 
				Integer.parseInt(low), lowBitsNum);
	}
	
	public static String compositeIntStr(String high, String low,
			int lowBitsNum) {
		return String.valueOf(compositeInt(high, low, lowBitsNum));
	}
	
	public static long compositeLong(long high, long low, int lowBitsNum) {
		if(low >>> lowBitsNum > 0) {
			throw new IllegalArgumentException("low:" + low + 
				" has exceed the up-limit with lowBitsNum:" + lowBitsNum);
		}
		if(high >>> (63 - lowBitsNum) > 0) {
			throw new IllegalArgumentException("high:" + high + 
				" has exceed the up-limit with lowBitsNum:" + lowBitsNum);
		}
		return high << lowBitsNum | low;
	}
	
	public static long compositeLong(String high, String low, int lowBitsNum) {
		return compositeLong(
				Long.parseLong(high), Long.parseLong(low), lowBitsNum);
	}
	
	public static String compositeLongStr(String high, String low,
			int lowBitsNum) {
		return String.valueOf(compositeLong(high, low, lowBitsNum));
	}

	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(get(
			Integer.valueOf("01101000", 2), 0, 9)));
	}

}

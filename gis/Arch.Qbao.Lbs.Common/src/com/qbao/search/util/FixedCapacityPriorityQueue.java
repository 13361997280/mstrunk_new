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
 * @Create-at 2011-11-23 11:32:02
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-11-23 11:32:02  	li_yao		1.0			Newly created
 */
public class FixedCapacityPriorityQueue<T extends Comparable<T>> extends 
	PriorityQueue<T> {


	public FixedCapacityPriorityQueue(T[] initHeap){
		heap = initHeap;
	}
	
	@Override
	protected void ensureCapacity(int newSize) {
		//do nothing
	}

}

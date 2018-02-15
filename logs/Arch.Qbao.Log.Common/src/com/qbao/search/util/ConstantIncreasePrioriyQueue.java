package com.qbao.search.util;

import java.util.Arrays;

/**
 * @Description
 * In this queue, memory is top-priority,so dynamic extend 
 * heap size if nessary,if you will ceate lots of priority queue,you
 * can use this queue,though a bit slower but fewer memory-used
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-8-5 10:02:00
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 10:02:00  	li_yao		1.0			Newly created
 */
public class ConstantIncreasePrioriyQueue<T extends Comparable<T>> 
	extends PriorityQueue<T> {
	
	final protected int increaseStep;

	public ConstantIncreasePrioriyQueue(T[] initHeap){
		increaseStep = initHeap.length - 1;
		heap = initHeap;
	}
	
	protected void ensureCapacity(int newSize){
		if(newSize > heap.length - 1){//the last
			int newLength = Math.max(heap.length + increaseStep, newSize + 1);
			heap = Arrays.copyOf(heap, newLength);
		}
	}

}

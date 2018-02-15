package com.qbao.search.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class SlideWindowCounter {	
	final protected int bucketSeconds;
	final protected AtomicInteger[] buckets;
	
	private SlideWindowCounter(int periodSeconds, int bucketSeconds) {
		if(!(periodSeconds > bucketSeconds &&  bucketSeconds > 0 &&
				periodSeconds/bucketSeconds > 1)) {
			throw new IllegalArgumentException("Illegal arguments," +
				"periodSeconds:" + periodSeconds + ",bucketSeconds:" +
				bucketSeconds);
		}
		this.bucketSeconds = bucketSeconds;
		buckets = new AtomicInteger[periodSeconds/bucketSeconds];
		for(int i = 0; i < buckets.length; i++) {
			buckets[i] = new AtomicInteger();
		}

	}
	
	protected int getBucketIndex() {
		return (int)(System.currentTimeMillis() / 1000 / bucketSeconds)
				% buckets.length;
	}
	
	public void setCount() {
		for(int i = 0; i < buckets.length; i++) {
			buckets[i].incrementAndGet();
		}
	}
	
	public int getCount() {
		return buckets[getBucketIndex()].get();
	}
	
	protected void resetBucket() {
		int bucketIndex = getBucketIndex() - 1;
		if(bucketIndex < 0) {
			bucketIndex = buckets.length - 1;
		}
		buckets[bucketIndex].set(0);
	}
	
	private static class Entry {
		final private Timer timer;
		final private List<SlideWindowCounter> counters;
		public Entry(int bucketSeconds) {
			timer =  new Timer("SlideWindowCounter", true);
			counters = new ArrayList<SlideWindowCounter>();
			timer.scheduleAtFixedRate(
				new TimerTask(){
					@Override
					public void run() {
						for(int i = 0; i < counters.size(); i++) {
							SlideWindowCounter counter = counters.get(i);
							if(counter != null) {
								counter.resetBucket();
							}
						}
					}	
				}, 
				2000, bucketSeconds * 1000
			);
		}
		
		public synchronized void addCounter(SlideWindowCounter counter) {
			counters.add(counter);
		}
		
	}
	
	final private static Map<Integer, Entry> ENTRYS 
								= new HashMap<Integer, Entry>();
	
	public static SlideWindowCounter get(int periodSeconds, int bucketSeconds) {
		SlideWindowCounter counter = 
				new SlideWindowCounter(periodSeconds, bucketSeconds);
		Entry entry = ENTRYS.get(bucketSeconds);
		if(entry == null) {
			synchronized (ENTRYS) {
				entry = ENTRYS.get(bucketSeconds);
				if(entry == null) {
					entry = new Entry(bucketSeconds);
					ENTRYS.put(bucketSeconds, entry);
				}
			}
		}
		entry.addCounter(counter);
		return counter;
	}
}

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
 * @Create-at 2011-8-5 10:02:40
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 10:02:40  	li_yao		1.0			Newly created
 */
public abstract class PriorityQueue<T extends Comparable<T>> {
	
	protected int size;
	protected T[] heap;	

	protected abstract void ensureCapacity(int newSize);

	
	public final void fillSentinelObject(T[] sentinelObjects) {
		if(size > 0){
			throw new UnsupportedOperationException(
				"can't fill priority queue which has already add object");
		}
		ensureCapacity(sentinelObjects.length);
		for (int i = 0; i < sentinelObjects.length; i++) {
			heap[i + 1] = sentinelObjects[i];
		}
		size = sentinelObjects.length;
	}
	

	/**
	 * Adds an Object to a PriorityQueue in log(size) time. If one tries to add
	 * more objects than maxSize from initialize an
	 * {@link ArrayIndexOutOfBoundsException} is thrown.
	 * 
	 * @return the new 'top' element in the queue.
	 */
	public T add(T element) {
		ensureCapacity(++size);
		heap[size] = element;
		upHeap();
		return heap[1];
	}
	
	public T updateTop(T e){
		//assume queue not empty
		if(e.compareTo(heap[1]) > 0){
			heap[1] = e;
			downHeap();
		}
		return heap[1];
	}

	/** Returns the least element of the PriorityQueue in constant time. */
	public final T top() {
		// We don't need to check size here: if maxSize is 0,
		// then heap is length 2 array with both entries null.
		// If size is 0 then heap[1] is already null.
		return heap[1];
	}

	/** Removes and returns the least element of the PriorityQueue in log(size)
		time. */
	public final T pop() {
		if (size > 0) {
			T result = heap[1];				// save first value
			heap[1] = heap[size];				// move last to first
			heap[size] = null;				// permit GC of objects
			size--;
			downHeap();					// adjust heap
			return result;
		} else
			return null;
	}
	
	/**
	 * Should be called when the Object at top changes values. 
	 * Still log(n) worst case, but it's at least twice as fast to
	 * 
	 * <pre>
	 * pq.top().change();
	 * pq.updateTop();
	 * </pre>
	 * 
	 * instead of
	 * 
	 * <pre>
	 * o = pq.pop();
	 * o.change();
	 * pq.push(o);
	 * </pre>
	 * 
	 * @return the new 'top' element.
	 */
	public final T updateTop() {
		downHeap();
		return heap[1];
	}

	/** Returns the number of elements currently stored in the PriorityQueue. */
	public final int size() {
		return size;
	}

	/** Removes all entries from the PriorityQueue. */
	public final void clear() {
		for (int i = 0; i <= size; i++) {
			heap[i] = null;
		}
		size = 0;
	}


	private final void upHeap() {
		upHeap(size);
	}

	private final void downHeap() {
		downHeap(1);
	}
	
	protected final void upHeap(int i) {
		T node = heap[i];				// save the node
		int j = i >>> 1;
		while (j > 0 && node.compareTo(heap[j]) < 0) {
			heap[i] = heap[j];				// shift parents down
			i = j;
			j = j >>> 1;
		}
		heap[i] = node;					// install saved node
	}
	
	protected final void downHeap(int i) {
		T node = heap[i];				// save the node
		int j = i << 1;					// find smaller child
		int k = j + 1;
		if (k <= size && heap[k].compareTo(heap[j]) < 0) {
			j = k;
		}
		while (j <= size && heap[j].compareTo(node) < 0) {
			heap[i] = heap[j];				// shift up child
			i = j;
			j = i << 1;
			k = j + 1;
			if (k <= size && heap[k].compareTo(heap[j]) < 0) {
				j = k;
			}
		}
		heap[i] = node;					// install saved node
	}
	
	/**
	 * assume i is legal
	 * @param i
	 */
	public final void changeHeap(int i) {
		if(i == 1) {
			downHeap(1);
		} else {
			if(heap[i].compareTo(heap[i >>> 1]) < 0) {//less than parent
				upHeap(i);
			} else {
				downHeap(i);
			}
		}
	}

}

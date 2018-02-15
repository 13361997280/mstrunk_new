package com.qbao.search.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
 * @Create-at 2011-8-5 10:04:03
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 10:04:03  	li_yao		1.0			Newly created
 */
public class ConcurrentUtil {
	
//	final public static ExecutorService executor = Executors.newCachedThreadPool() ;
//	final public static ExecutorService executor2 = new ThreadPoolExecutor( 
//			Config.get().getInt("concurrent.executor.thread.num", 8), 
//			Config.get().getInt("concurrent.executor.thread.num", 8),
//			0L, TimeUnit.MILLISECONDS,
//			new LinkedBlockingQueue<Runnable>( Config.get().getInt("concurrent.executor.queue.num",100000))
//	);
	
	/**
	 * sync
	 * @param <T>
	 * @param taskList
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static <T>List<T> execute(ExecutorService executor,
			List<Callable<T>> taskList)
		throws InterruptedException, ExecutionException{
		
		return get(asyncExecute(executor, taskList) );
	}
	
	/**
	 * async
	 * @param <T>
	 * @param taskList
	 * @return
	 */
	public static <T>List<Future<T>> asyncExecute(
			ExecutorService executor, List<Callable<T>> taskList ){
		List<Future<T>> futureList = 
			new ArrayList<Future<T>>( taskList.size() );
		for( int i = 0; i < taskList.size(); i++ ){
			futureList.add( executor.submit(taskList.get( i ) ) );
		}
		return futureList;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param futureList
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static <T>List<T> get( List<Future<T>> futureList )
		throws InterruptedException, ExecutionException{
		
		List<T> respList= new ArrayList<T>(futureList.size());
		
		for( int i = 0; i < futureList.size(); i++ ){
			respList.add(futureList.get( i ).get() );
		}
		return respList;
		
	}
	
}

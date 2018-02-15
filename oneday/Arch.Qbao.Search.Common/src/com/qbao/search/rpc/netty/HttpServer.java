package com.qbao.search.rpc.netty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.logging.support.AbstractESLogger;
import com.qbao.search.util.CommonUtil;

/**
 * 
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
 * @Create-at 2011-8-5 09:59:51
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 09:59:51  	li_yao		1.0			Newly created
 */
public abstract class HttpServer extends AbstractServer{

	private static ESLogger logger = Loggers.getLogger(HttpServer.class);
	
	public static String CLIENT_IP_HEADER = "Netty-Client-IP";
	
	private final int maxRequestLen;
	
	private ExecutorService executor;
	private Object executorLock = new Object();
	
	protected boolean sendException;
	
	protected final String deployVersion;
	
	
    static class HttpHandler extends SimpleChannelUpstreamHandler{
    	HttpServer server;
    	long accessTime;
    	Object errorTag = new Object();
    	
    	HttpHandler(HttpServer server, long accessTime) {
    		this.server = server;
    		this.accessTime = accessTime;
    	}
    	
    	@Override
    	public void messageReceived(ChannelHandlerContext ctx, 
    			MessageEvent e)throws Exception {
    		HttpRequest httpRequest = (HttpRequest) e.getMessage();
    		
    		//transfer parameters to headers for reuse
    		httpRequest.setHeader(CommonUtil.PARAM_HEADER, "true");
    		String encodeing = "UTF-8";
    		Map<String, List<String>> paramMap = new QueryStringDecoder(
    				httpRequest.getUri(), 
    				Charset.forName(encodeing)).getParameters();
    		for(Entry<String, List<String>> entry:paramMap.entrySet()){
    			if(!entry.getKey().isEmpty() && entry.getValue() != null &&
    					entry.getValue().size() > 0) {
    				httpRequest.setHeader(CommonUtil.PARAM_HEADER +
    					entry.getKey().toLowerCase(), entry.getValue().get(0));
    			}
    		}
    		
    		if(!CommonUtil.getBooleanParam(httpRequest, "noLogging") &&
    				HttpUtil.getUri(httpRequest.getUri()).matches(
    				Config.get().get("access.logged.uri.reges", ".*"))) {
    			httpRequest.setHeader("access-logged", true);
    			if(httpRequest.getContent() != null) {
    				httpRequest.setHeader("request-content-length", 
    						httpRequest.getContent().readableBytes());
        			if(HttpUtil.getUri(httpRequest.getUri()).matches(
            			Config.get().get("access.content.logged.uri.reges",
            				"(?!.)"))) {
            			httpRequest.setHeader("request-content-index-for-log", 
            				httpRequest.getContent().readerIndex() + "-" +
            				httpRequest.getContent().writerIndex());
                	}
    			}
    		}

    		HttpRequestParser parser = server.getHttpRequestParser();
    		//bind client IP
    		httpRequest.removeHeader(CLIENT_IP_HEADER);
    		httpRequest.setHeader(CLIENT_IP_HEADER,
    				CommonUtil.getRemoteIP(e.getChannel()));
			HttpRequestHandler handler = parser.parse(httpRequest);
			handler.setServer(server);
			handler.setTimeStamp(accessTime);
			handler.setRequest(httpRequest);
			handler.setChannel(e.getChannel());
			handler.setSendException(server.sendException);
			
			ExecutorService executor = parser.getExecutor(handler.getClass());
			if(executor == null){
				executor = server.getExecutor();
			}
			executor.submit(handler);
    	}

    	@Override
    	public void exceptionCaught(ChannelHandlerContext ctx,
    			ExceptionEvent e) throws Exception{
    		//async netty operation exception caught may lead to dead snoop
    		//so should tag the exception to avoid the case
    		Object tag = ctx.getAttachment();
    		if(tag == null){
    			if(e.getCause() instanceof IllegalAccessException ||
    					e.getCause() instanceof IllegalArgumentException) {
    				logger.warn("{}-first exceptionCaught,send error",
    					e.getCause(), ctx.getChannel());
    			} else {
    				logger.error("{}-first exceptionCaught,send error",
        				e.getCause(), ctx.getChannel());
    			}
    			ctx.setAttachment(errorTag);
        		HttpUtil.sendHttpResponse(ctx.getChannel(), null, e.getCause(),
        			server.sendException, accessTime);
    		} else if(tag == errorTag){
        		ctx.getChannel().close();//async
    			logger.warn("{}-has already caught exception," +
    				"don't send error any more in case the dead loop " +
    				"just close the channel", e.getCause(), ctx.getChannel());
    		} else {
        		ctx.getChannel().close();//async
    			logger.error("{}-unexpected attachment:{} bind,close the " +
    				"channel", e.getCause(), ctx.getChannel());
    		}
    		
    	}

    };
	
	
	public HttpServer(String serverName, boolean sendException) {
		super(serverName);
		maxRequestLen = Config.get().getInt(
			"server.request.max.length.bytes", 100*1024*1024);
		this.sendException = sendException;
		BufferedReader br = null;
		String version = null;
		try {
			//br = new BufferedReader(new FileReader("../deploy.version"));
			version = "lexis1.0";
		} finally {
			deployVersion = version;
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}


	protected abstract HttpRequestParser getHttpRequestParser();

	
	protected ExecutorService getExecutor(){
		if(executor == null){
			synchronized(executorLock){
				if(executor == null){
					executor = new ThreadPoolExecutor(
						Config.get().getInt("server.executor.pool.core.size", 
								Runtime.getRuntime().availableProcessors()),
						Config.get().getInt("server.executor.pool.max.size", 
							Runtime.getRuntime().availableProcessors() * 20),
						60L,
						TimeUnit.SECONDS, 
						new LinkedBlockingQueue<Runnable>(Config.get().getInt(
								"server.executor.queue.size", 5000))
					);
				}
			}
		}
		return executor;
	}
	

	@Override
	public ChannelCollectablePipelineFactory createPipelineFactory() {
		return new ChannelCollectablePipelineFactory(){
			
			@Override
			protected void processPipeline(final ChannelPipeline pipeline,
					long accessTime) {
		        pipeline.addLast("decoder", new HttpRequestDecoder());
		        pipeline.addLast("aggregator", 
		        		new HttpChunkAggregator(maxRequestLen));
		        pipeline.addLast("encoder", new HttpResponseEncoder());
		        pipeline.addLast("deflater", new HttpContentCompressor());
    			pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		        
    			pipeline.addLast("lastHanler",
    				new HttpHandler(HttpServer.this, accessTime));
			}
			
		};
	}
	
	
	@Override
	public void getMonitorInfos(String prefix, Map<String, Object> map){
		super.getMonitorInfos(prefix, map);
		prefix += HttpServer.class.getSimpleName() + ".";
		map.put(prefix + "deploy.version", deployVersion);
		map.put(prefix + "getHttpRequestParser()", getHttpRequestParser());
		map.put(prefix + "maxRequestLen", maxRequestLen);
		map.put(prefix + "sendException", sendException);
		map.put(prefix + "server.executor.queue.info",
			CommonUtil.getThreadPoolInfo((ThreadPoolExecutor) getExecutor()));
		map.put(prefix + "log4j.executor.queue.info",
			CommonUtil.getThreadPoolInfo(AbstractESLogger.executor));
	}

}

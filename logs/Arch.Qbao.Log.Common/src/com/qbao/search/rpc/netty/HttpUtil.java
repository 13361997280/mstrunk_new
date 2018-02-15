package com.qbao.search.rpc.netty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.CommonUtil;

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
 * @Create-at 2012-3-28 15:57:27
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2012-3-28 15:57:27  	li_yao		1.0			Newly created
 */
public class HttpUtil {
	private static ESLogger logger = Loggers.getLogger(HttpUtil.class);
	
	private static ESLogger accessLogger = Loggers.getLogger("SearchHandler");
	
	public static HttpResponse getHttpResponse(Object response,
			boolean sendException, long accessTime) {
		HttpResponse httpResponse = null;
		if(response instanceof HttpResponse){
			httpResponse = (HttpResponse) response;
		}
		else {
			int i = 0;
			final int maxLoop = 5;
			for(; i < maxLoop; i++) {
				try {
					HttpResponseStatus status;
					if(response instanceof Throwable){
						status = response instanceof IllegalAccessException ?
							HttpResponseStatus.NOT_FOUND :
							HttpResponseStatus.EXPECTATION_FAILED;
						response = sendException ? CommonUtil.toString(
							(Throwable) response) : status.toString();
					} else{
						status = HttpResponseStatus.OK;
					}
					
					ChannelBuffer buffer;
					if(response != null){
						if(response instanceof ChannelBuffer){
							buffer = (ChannelBuffer) response;
						} else if(response instanceof byte[]){
							buffer = 
								ChannelBuffers.wrappedBuffer((byte[]) response);
						} else{
							buffer = ChannelBuffers.copiedBuffer(
								response.toString(), CharsetUtil.UTF_8);
						}
					} else{
						buffer = ChannelBuffers.EMPTY_BUFFER;
					}
					
					httpResponse = 
						new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
					httpResponse.setHeader("Content-Length",
							buffer.writerIndex());
					httpResponse.setContent(buffer);
					break;
				} catch(Throwable t) {
					logger.error(t);
					response = t;
				}
			}
			if(i == maxLoop) {
				httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
					HttpResponseStatus.EXPECTATION_FAILED);
			}
		}
		httpResponse.addHeader("Time-Used", 
				(System.currentTimeMillis() - accessTime) + "ms");
		return httpResponse;
	}
	
	/**
	 * nevel throw exception
	 * @param channel
	 * @param response
	 * @param sendException
	 */
	public static void sendHttpResponse(Channel channel, HttpRequest httpRequest,
				Object response, boolean sendException, long accessTime) {
		String responseStatus = "No-Response";
		long responseSize = -1;
		try {
			if(channel == null || !channel.isOpen() ) {
				return;
			}
			HttpResponse httpResonse = getHttpResponse(response, sendException,
					accessTime);
			responseStatus = httpResonse.getStatus().toString();
			responseSize = httpResonse.getContent().readableBytes();
			if (!channel.isConnected()){
				return;
			}
			channel.write(httpResonse).addListener(ChannelFutureListener.CLOSE);
		} catch(Throwable t) {
			try {
				channel.close();
				logger.error(t);
			} catch(Throwable t2) {
				//do nothing
			}
		} finally {//logging
			try {
				logAccess(channel, httpRequest, responseStatus, responseSize,
					accessTime);
			} catch(Exception e) {
				
			}
		}
	}
	
	public static String getUri(String url) {
		int pos = url.indexOf('?');
		if(pos >= 0){
			url = url.substring(0, pos);
		}
		if(url.charAt(url.length() - 1) == '/'){
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}
	
	static void logAccess(Channel channel, HttpRequest httpRequest,
			String responseStatus, long responseSize, long accessTime) {
		if(httpRequest == null ||
				httpRequest.getHeader("access-logged") == null) {
			return;
		}
		httpRequest.removeHeader("access-logged");
		
		int logRequestContentLen = 300;
		String requestStr = null;
		String rwIndex = httpRequest.getHeader("request-content-index-for-log");
		if(rwIndex != null) {
			int[] fromTo = CommonUtil.genFromTo(rwIndex);
			httpRequest.getContent().readerIndex(fromTo[0]);
			httpRequest.getContent().writerIndex(fromTo[1]);
			logRequestContentLen += httpRequest.getContent().readableBytes();
			requestStr = httpRequest.getContent().toString(CharsetUtil.UTF_8);
			httpRequest.removeHeader("request-content-index-for-log");
		}
		
		String requestContentLen = 
			httpRequest.getHeader("request-content-length");
		httpRequest.removeHeader("request-content-length");
		
		String logDateFormat = Config.get().get("access.log.date.format",
				"yyyy-MM-dd HH:mm:ss.SSS");
		String logRecordSep = Config.get().get("access.log.record.separator",
				"#");
		String logItemSep = Config.get().get("access.log.item.separator",
				"\t");
		String logHttpHeadSep = 
			Config.get().get("access.log.item.http.header.separator", ";");
			
		StringBuilder sb = new StringBuilder(logRequestContentLen);
		sb.append(logRecordSep);
		
		sb.append(new SimpleDateFormat(logDateFormat).format(
				new Date(accessTime)));//access time
		
		sb.append(logItemSep);
		sb.append(channel.getLocalAddress());//local address
		
		sb.append(logItemSep);
		sb.append(channel.getRemoteAddress());//remote address
		
		sb.append(logItemSep);
		sb.append(CommonUtil.getRemoteIP(httpRequest));//user IP
		
		sb.append(logItemSep);
		sb.append(responseStatus);//HttpResonse Status
		
		sb.append(logItemSep);
		sb.append(System.currentTimeMillis() - accessTime);//usedTime
		
		sb.append(logItemSep);
		sb.append(responseSize);//response size
		
		sb.append(logItemSep);
		sb.append(requestContentLen);//request size
		
		sb.append(logItemSep);
		sb.append(httpRequest.getMethod());//method
		
		sb.append(logItemSep);
		sb.append(httpRequest.isChunked());//chunked
		
		sb.append(logItemSep);
		sb.append(httpRequest.getUri());//URI
		
		sb.append(logItemSep);//protocol version
		sb.append(httpRequest.getProtocolVersion().getText());
		
		sb.append(logItemSep);
        for (Map.Entry<String, String> e: httpRequest.getHeaders()) {
        	if(!e.getKey().startsWith(CommonUtil.PARAM_HEADER)) {
	            sb.append(e.getKey());
	            sb.append(":");
	            sb.append(e.getValue());
				sb.append(logHttpHeadSep);
        	}
        }
		if(requestStr != null) {
			sb.append(logItemSep);
			sb.append(requestStr);//request content
		}
		
		accessLogger.info(sb.toString());
	}

}

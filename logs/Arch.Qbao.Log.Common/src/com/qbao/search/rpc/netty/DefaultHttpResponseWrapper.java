/**
 * 
 */
package com.qbao.search.rpc.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

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
 * @Create-at 2011-12-7 17:11:48
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-7 17:11:48  	li_yao		1.0			Newly created
 */
@Deprecated
public class DefaultHttpResponseWrapper implements HttpResponseWrapper {

	boolean sendException;

	/**
	 * @param sendException
	 */
	public DefaultHttpResponseWrapper(boolean sendException) {
		this.sendException = sendException;
	}

	@Override
	public HttpResponse wrap(HttpRequest httpRequest, Object response) {
		if(response instanceof HttpResponse){
			return (HttpResponse) response;
		}
		HttpResponseStatus status = HttpResponseStatus.OK;
		if(response instanceof Exception){
			status = HttpResponseStatus.EXPECTATION_FAILED;
			response = sendException ? 
					CommonUtil.toString((Throwable) response) 
					: status.toString();
		}
		else{
			status = HttpResponseStatus.OK;
		}
		
		ChannelBuffer buffer;
		if(response != null){
			if(response instanceof ChannelBuffer){
				buffer = (ChannelBuffer) response;
			}
			else{
				buffer = ChannelBuffers.copiedBuffer(response.toString(),
						CharsetUtil.UTF_8);
			}
		}
		else{
			buffer = ChannelBuffers.EMPTY_BUFFER;
		}
		
		HttpResponse httpResponse = new DefaultHttpResponse(
				HttpVersion.HTTP_1_1, status);
		httpResponse.setHeader("Content-Type", "text/html; charset=UTF-8");
		httpResponse.setHeader("Content-Length", buffer.writerIndex());
		httpResponse.setContent(buffer);
		return httpResponse;
	}

}

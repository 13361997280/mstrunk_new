package com.qbao.search.rpc.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.AbstractRequestHandler;

public abstract class HttpRequestHandler extends AbstractRequestHandler {
	
	
	protected HttpRequest httpRequest;
	
	protected Channel channel;
	
	protected boolean sendException = false;

	@Override
	final public void setRequest(Object request) throws Exception {
		httpRequest = (HttpRequest)request;
	}
	
	
	public void setChannel(Channel channel){
		this.channel = channel;
	}

	
	public void setSendException(boolean sendException){
		this.sendException = sendException;
	}
	


}

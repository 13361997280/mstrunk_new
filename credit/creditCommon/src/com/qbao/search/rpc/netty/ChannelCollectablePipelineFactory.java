package com.qbao.search.rpc.netty;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;

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
 * @Create-at 2011-8-5 09:59:30
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 09:59:30  	li_yao		1.0			Newly created
 */
public abstract class ChannelCollectablePipelineFactory 
	implements ChannelPipelineFactory {

	protected ChannelGroup allChannels;
	
	private ChannelHandler channelCollector = 
		new SimpleChannelUpstreamHandler(){
		    public void channelOpen(ChannelHandlerContext ctx, 
		    		ChannelStateEvent e) throws Exception {
		       allChannels.add( e.getChannel() );
		       ctx.sendUpstream(e);	       
		    }
		};
	
	public void setAllChannels(ChannelGroup allChannels) {
		this.allChannels = allChannels;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		long accessTime = System.currentTimeMillis();
		ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("channelCollector", channelCollector );
        processPipeline(pipeline, accessTime);
		return pipeline;
	}
	
	protected abstract void processPipeline(ChannelPipeline pipeline,
			long accessTime);

}

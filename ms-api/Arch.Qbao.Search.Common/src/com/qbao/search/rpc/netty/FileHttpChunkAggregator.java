/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.qbao.search.rpc.netty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * A {@link ChannelHandler} that aggregates an {@link HttpMessage}
 * and its following {@link HttpChunk}s into a single {@link HttpMessage} with
 * no following {@link HttpChunk}s.  It is useful when you don't want to take
 * care of HTTP messages whose transfer encoding is 'chunked'.  Insert this
 * handler after {@link HttpMessageDecoder} in the {@link ChannelPipeline}:
 * <pre>
 * {@link ChannelPipeline} p = ...;
 * ...
 * p.addLast("decoder", new {@link HttpRequestDecoder}());
 * p.addLast("aggregator", <b>new {@link FileHttpChunkAggregator}(1048576)</b>);
 * ...
 * p.addLast("encoder", new {@link HttpResponseEncoder}());
 * p.addLast("handler", new HttpRequestHandler());
 * </pre>
 *
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @version $Rev: 2354 $, $Date: 2010-08-26 14:06:40 +0900 (Thu, 26 Aug 2010) $
 *
 * @apiviz.landmark
 * @apiviz.has org.jboss.netty.handler.codec.http.HttpChunk oneway - - filters out
 */
public class FileHttpChunkAggregator extends SimpleChannelUpstreamHandler {
	private static ESLogger logger = 
			Loggers.getLogger(FileHttpChunkAggregator.class);
	
	private String dstPath;
	
    //private final int maxContentLength;
    
    private HttpMessage currentMessage;
    
    private BufferedOutputStream dos;
    
    private final int bufferSize;
    
    private int receivedLen = 0;

    /**
     * 
     * @param dstPath the destination of the file that save the received content
     * @param bufferSize the size buffered in the ram
     */
    public FileHttpChunkAggregator(String dstPath, int bufferSize) {
        
    	this.dstPath = dstPath;
    	
    	if (bufferSize <= 0) {
            throw new IllegalArgumentException(
                "bufferSize must be a positive integer: " + bufferSize);
        }
    	this.bufferSize = bufferSize;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        

    	if(dos == null){
			File file = new File(dstPath);
			File parent = file.getParentFile();
			parent.mkdirs();
			dos = new BufferedOutputStream(new FileOutputStream(file),
					bufferSize);
    	}
    

        Object msg = e.getMessage();
        HttpMessage currentMessage = this.currentMessage;

        if (msg instanceof HttpMessage) {
            HttpMessage m = (HttpMessage) msg;
            ChannelBuffer buf = m.getContent();
            if(buf.readableBytes() > 0){
            	buf.readBytes(dos , buf.readableBytes());
                dos.close();
            }
            
            if (m.isChunked()) {
                // A chunked message - remove 'Transfer-Encoding' header,
                // initialize the cumulative buffer,
            	// and wait for incoming chunks.
                List<String> encodings = m.getHeaders(
                		HttpHeaders.Names.TRANSFER_ENCODING);
                encodings.remove(HttpHeaders.Values.CHUNKED);
                if (encodings.isEmpty()) {
                    m.removeHeader(HttpHeaders.Names.TRANSFER_ENCODING);
                }
                m.setChunked(false);
                this.currentMessage = m;
            } else {
                // Not a chunked message - pass through.
                this.currentMessage = null;
                ctx.sendUpstream(e);
            }
        } else if (msg instanceof HttpChunk) {
            // Sanity check
            if (currentMessage == null) {
                throw new IllegalStateException(
                        "received " + HttpChunk.class.getSimpleName() +
                        " without " + HttpMessage.class.getSimpleName());
            }

            HttpChunk chunk = (HttpChunk) msg;
            
            //content.writeBytes(chunk.getContent());
            ChannelBuffer buf = chunk.getContent();
            receivedLen += buf.readableBytes();
            buf.readBytes(dos , buf.readableBytes());
            if (chunk.isLast()) {
                this.currentMessage = null;

                // Merge trailing headers into the message.
                if (chunk instanceof HttpChunkTrailer) {
                    HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
                    for(Entry<String, String> header: trailer.getHeaders()) {
                        currentMessage.setHeader(header.getKey(),
                        		header.getValue());
                    }
                }

                // Set the 'Content-Length' header.
                currentMessage.setHeader(
                        HttpHeaders.Names.CONTENT_LENGTH,
                        String.valueOf(receivedLen));

                // All done - generate the event.
                Channels.fireMessageReceived(ctx, currentMessage,
                		e.getRemoteAddress());
                dos.close();
            }
        } else {
        	logger.trace("a http request is non a HttpMessage nor a HttpChunk");
            // Neither HttpMessage or HttpChunk
            ctx.sendUpstream(e);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
    	dos.close();
    	ctx.sendUpstream(e);
    }
}

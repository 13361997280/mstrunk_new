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

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.FileRegion;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 */
public class HttpDownloadHandler extends SimpleChannelUpstreamHandler {
	
	private static ESLogger logger = 
		Loggers.getLogger(HttpDownloadHandler.class);
	
	final protected ExecutorService downloadExecutor = 
		new ThreadPoolExecutor(0, 1, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(10));
	
	protected String downloadRootPath;
	
    /**
	 * @param downloadRootPath
	 */
	public HttpDownloadHandler(String downloadRootPath) {
		this.downloadRootPath = downloadRootPath;
	}


	@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	final ChannelHandlerContext fctx = ctx;
    	final MessageEvent fe = e;
    	downloadExecutor.submit(new Runnable(){

			@Override
			public void run() {
				try {
					processMessage(fctx, fe);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
    		
    	});
    }


    void processMessage(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();
        if (request.getMethod() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        final String srcPath = sanitizeUri(request.getUri());
        System.out.println("path:" + srcPath);
        if (srcPath == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        File file = new File(srcPath);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        
        final long startTime = System.currentTimeMillis();
        long fileLength = file.length();
        final Channel channel = e.getChannel();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(response, fileLength);

        // Write the initial line and the header.
        channel.write(response);    

        // Write the content - use zero-copy.
        long batchNum = 1024*1024*10;
        long start = 0;
        while( start < fileLength ){
        	long len = Math.min( batchNum , fileLength - start );
        	final boolean lastBatch = start + len >= fileLength;
            final RandomAccessFile raf = new RandomAccessFile( file , "r");
	        final FileRegion region = new DefaultFileRegion(
	        		raf.getChannel(), start, len);
	        ChannelFuture writeFuture = channel.write(region);
	        writeFuture.addListener(new ChannelFutureProgressListener() {
	        	
	            public void operationComplete(ChannelFuture future) {
	                region.releaseExternalResources();
	                try {
						raf.close();
					} catch (IOException e) {
						logger.error(e);
					}
					if( lastBatch ){
						logger.trace("last batch,finish downoad file:{}," +
							"used time:{}s, close the channel", srcPath,
							(System.currentTimeMillis() - startTime)/1000);
						channel.close();
					}
	            }
	
	            public void operationProgressed(
	                    ChannelFuture future, long amount, long current,
	                    long total) {
	                logger.trace("%s: %d / %d (+%d)%n", srcPath,
	            	current, total, amount);
	            }
	        });

	        start += len;
        }
        

//        // Decide whether to close the connection or not.
//        if (!isKeepAlive(request)) {
//	        // Close the connection when the whole content is written out.
//	        writeFuture.addListener(ChannelFutureListener.CLOSE);
//	        System.out.println("channel closed!");
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        cause.printStackTrace();
        if (ch.isConnected()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private String sanitizeUri(String uri) {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
		String path = new QueryStringDecoder(uri).getParameters().get(
				"path").get(0);
        // Convert file separators.
        path = path.replace('/', File.separatorChar);

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (path.contains(File.separator + ".") ||
            path.contains("." + File.separator) ||
            path.startsWith(".") || path.endsWith(".")) {
            return null;
        }

        // Convert to absolute path.
        return downloadRootPath + File.separator + path;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8));

        // Close the connection as soon as the error message is sent.
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }
}

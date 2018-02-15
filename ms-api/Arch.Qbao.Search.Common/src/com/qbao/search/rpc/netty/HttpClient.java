package com.qbao.search.rpc.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.qbao.search.conf.Config;


public class HttpClient {
	
	//private static ESLogger logger = Loggers.getLogger(HttpClient.class);

	private final ChannelGroup allChannels;
	
	private final ChannelFactory channelFactory;
	
	private final ChannelHandler lastHandler;
	
	private final Timer timer = new HashedWheelTimer();
	
	private static HttpClient SINGLETON;
	
	public static HttpClient get() {
		if(SINGLETON == null) {
			synchronized (HttpClient.class) {
				if(SINGLETON == null) {
					SINGLETON = new HttpClient();
				}
			}
		}
		return SINGLETON;
	}
	
	
	private ChannelPipeline getPipeline(String dstPath, int timeoutSeconds)
			throws Exception {
        //Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();
        
        if(timeoutSeconds > 0) {
        	pipeline.addLast("readTimeout", 
        		new ReadTimeoutHandler(timer, timeoutSeconds));
        }

        pipeline.addLast("codec", new HttpClientCodec());

        //Remove the following line if you don't want
        //automatic content decompression.
        pipeline.addLast("inflater", new HttpContentDecompressor());
        
        if(dstPath == null){//common
            //Uncomment the following line
        	//if you don't want to handle HttpChunks.
            pipeline.addLast("aggregator", new HttpChunkAggregator(
            	Config.get().getInt("client.response.max.length.bytes",
            			100*1024*1024))
            );
        } else {//download
            pipeline.addLast("fileAggregator", new FileHttpChunkAggregator(
            		dstPath, 
                	Config.get().getInt("client.download.buffer.size",8192))
            );
        }
		
        pipeline.addLast("lastHandler",  lastHandler);
        
        return pipeline;
	}
	
	private HttpClient(){
		allChannels = new DefaultChannelGroup("HttpClient");
		
		channelFactory = new NioClientSocketChannelFactory (   
                Executors.newCachedThreadPool(),   
                Executors.newCachedThreadPool() 
        );
		
		
		lastHandler = new SimpleChannelUpstreamHandler() {

            public void exceptionCaught(ChannelHandlerContext ctx,
            		ExceptionEvent e)throws Exception{
            	//logger.error("lastHandler exceptionCaught:", e.getCause());
                ctx.setAttachment( e.getCause() );
                e.getChannel().close();
            }

            public void messageReceived(ChannelHandlerContext ctx,
            		MessageEvent e) throws Exception{
            	ctx.setAttachment( e.getMessage() );
            }

		};
		


	}
	
	private ClientBootstrap getBootstrap(final int timeoutSeconds){
		ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);

        // Set up the event pipeline factory.
		clientBootstrap.setPipelineFactory(
			new ChannelPipelineFactory() {
	
			    public ChannelPipeline getPipeline() throws Exception {
			        return HttpClient.this.getPipeline(null, timeoutSeconds);
			    }
			}
		);
		return clientBootstrap;
	}
	
	
	private ClientBootstrap getDownloadBootstrap(final String dstPath){
		ClientBootstrap downloadBootstrap = new ClientBootstrap(channelFactory);

        // Set up the event pipeline factory.
		downloadBootstrap.setPipelineFactory(
			new ChannelPipelineFactory() {
	
			    public ChannelPipeline getPipeline() throws Exception {
			        return HttpClient.this.getPipeline(dstPath, 
			        	Config.get().getInt("httpclient.download.timeout.seconds", 60));
			    }
			}
		);
		return downloadBootstrap;
	}
	
	public HttpResponse download(String url, String dstPath)throws Exception{		
		return send(url, "GET", null, null, getDownloadBootstrap(dstPath));
	}

	public String get(String url, int timeoutSeconds) throws Exception{
		return get(url, "GET", null, null, timeoutSeconds);
	}
	
	public String get(String url, String method, int timeoutSeconds) throws Exception{
		return get(url, method, null, null, timeoutSeconds);
	}
	
	public String get(String url, String method, Map<String, Object>
		headers, Object content, int timeoutSeconds) throws Exception{
		HttpResponse resp = send(url, method, headers, content, timeoutSeconds);
		String respContent = resp.getContent().toString(CharsetUtil.UTF_8);
        if(!resp.getStatus().equals(HttpResponseStatus.OK)){
        	throw new IllegalStateException("The http status:" 
        		+ resp.getStatus() + " is not OK(200)\n" + resp +
        		",content:" + respContent);
        }
		return respContent;
	}
	
	public HttpResponse send(String url, String method, Map<String, Object>
			headers, Object content, int timeoutSeconds) throws Exception{
		return send(url, method, headers, content, 
				getBootstrap(timeoutSeconds));
	}
	
	
	public ChannelFuture asyncSend(String url, String method, 
			Map<String, Object> headers, Object content, int timeoutSeconds)
				throws Exception{
		return asyncSend(url, method, headers, content, 
				getBootstrap(timeoutSeconds));
	}
	
	
	public HttpResponse send(String url, String method, Map<String, Object>
			headers, Object content, ClientBootstrap bootstrap)
			throws Exception{
        // Wait for the server to close the connection.
		ChannelFuture future = 
			asyncSend(url, method, headers, content, bootstrap);
		future.getChannel().getCloseFuture().awaitUninterruptibly();
        Object resp = future.getChannel().getPipeline().
        					getContext(lastHandler).getAttachment();
                
        if(resp instanceof Throwable){
        	throw (Exception)resp;
        }
        HttpResponse httpResp = (HttpResponse) resp;
        return (HttpResponse) httpResp;
	}

	
	public ChannelFuture asyncSend(String url, String method,
			Map<String, Object> headers, Object content,
			ClientBootstrap bootstrap)throws Exception{
		if(!url.startsWith("http")){
			url = "http://" + url;
		}
        final URI uri = new URI(url);
        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        if (!scheme.equalsIgnoreCase("http")) {
            throw new IllegalAccessException("Only HTTP is supported.");
        }
        String host = uri.getHost() == null? "localhost" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            port = 80;
        }

        // Prepare the HTTP request.
        final HttpRequest httpRequest = new DefaultHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.valueOf(method),
               url.substring(url.indexOf('/', 7)));
        if(headers != null){
	        for(Map.Entry<String, Object> entry:headers.entrySet()){
	        	httpRequest.setHeader(entry.getKey(), entry.getValue());
	        }
        }
        if(content != null){
        	ChannelBuffer chBufcontent = null;
        	if(content instanceof ChannelBuffer) {
        		chBufcontent = (ChannelBuffer) content;
        	} else {
        		chBufcontent = ChannelBuffers.copiedBuffer(
        			content.toString(),CharsetUtil.UTF_8);
        	}
        	httpRequest.setHeader(Names.CONTENT_LENGTH,
        			chBufcontent.readableBytes());
        	httpRequest.setContent(chBufcontent);
        }
        
        return this.asyncSend(host, port, httpRequest, bootstrap);
	}
	
	
	public ChannelFuture asyncSend(String host, int port, 
			HttpRequest httpRequest, ClientBootstrap bootstrap)throws Exception{
        // Start the connection attempt.
        final SocketAddress remoteAddr = new InetSocketAddress(host, port);
        ChannelFuture future = bootstrap.connect(remoteAddr);
        future.awaitUninterruptibly();  
        final Channel channel = future.getChannel();
        allChannels.add(channel);
        if (!future.isSuccess()) {
        	if(future.getCause() != null){
                throw (Exception)future.getCause();
        	}
        	throw new ConnectException("Failed to connect to:" + remoteAddr);
        }
        return channel.write(httpRequest);
	}
	
	public void close(){
		try {
			allChannels.close();
		} finally {
			timer.stop();
		}
	}

}

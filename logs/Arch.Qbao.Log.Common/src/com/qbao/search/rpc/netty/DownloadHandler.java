package com.qbao.search.rpc.netty;

import static org.jboss.netty.handler.codec.http.HttpHeaders.setContentLength;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.channel.FileRegion;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.util.CommonUtil;
import com.qbao.search.util.PathUtil;

public class DownloadHandler extends HttpRequestHandler {
	
	private static ESLogger logger = 
		Loggers.getLogger(DownloadHandler.class);
	
	protected static String downloadRootPath = PathUtil.getDataPath();


	protected void transfer() throws Exception {

        final String srcPath = sanitizeUri(httpRequest.getUri());

        File file = new File(srcPath);
        if (file.isHidden() || !file.exists() || !file.isFile()) {
        	throw new IllegalAccessException("Can't access to file:"+srcPath);
        }
        
        //final long startTime = System.currentTimeMillis();
        long fileLength = file.length();
        final Channel channel = this.channel;

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(response, fileLength);

        // Write the initial line and the header.
        if(fileLength == 0) {
        	 channel.write(response).addListener(ChannelFutureListener.CLOSE);
        	 return;
        }
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
//						logger.info("last batch,finish downoad file:{}," +
//							"used time:{}s, close the channel", srcPath,
//							(System.currentTimeMillis() - startTime)/1000);
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
//        }

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
				"filePath").get(0);
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


	@Override
	public void run() {
		try{
			//ChannelPipeline pipeline = channel.getPipeline();
			//pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
			transfer();
		}catch(Exception e){
			logger.error(e);
	        HttpResponse response = new DefaultHttpResponse(HTTP_1_1,
	        		HttpResponseStatus.EXPECTATION_FAILED);
	        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
	        response.setContent(ChannelBuffers.copiedBuffer(
	                "Failure: " + CommonUtil.toString(e) + "\r\n",
	                CharsetUtil.UTF_8));

	        // Close the connection as soon as the error message is sent.
	       channel.write(response).addListener(ChannelFutureListener.CLOSE);
		}
		
	}


	/* (non-Javadoc)
	 * @see com.ctrip.search.rpc.RequestHandler#setServer(com.ctrip.search.rpc.Server)
	 */
	@Override
	public void setServer(Server server) {
		// TODO Auto-generated method stub
		
	}

}

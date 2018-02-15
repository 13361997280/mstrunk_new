package com.qbao.search.rpc.netty;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * @author song.j
 * @create 2017-08-16 16:16:07
 **/
public class EditResponseHttpUtil extends HttpUtil {
    private static ESLogger logger = Loggers.getLogger(EditResponseHttpUtil.class);

    /**
     * nevel throw exception
     *
     * @param channel
     * @param response
     * @param sendException
     */
    public static void sendHttpResponse(Channel channel, HttpRequest httpRequest,
                                        HttpResponse response, boolean sendException, long accessTime) {
        String responseStatus = "No-Response";
        long responseSize = -1;
        try {
            responseStatus = response.getStatus().toString();
            responseSize = response.getContent().readableBytes();
            if (channel == null || !channel.isOpen() || !channel.isConnected()) {
                return;
            }
            channel.write(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Throwable t) {
            try {
                channel.close();
                logger.error(t);
            } catch (Throwable t2) {
                //do nothing
            }
        } finally {//logging
            try {
                logAccess(channel, httpRequest, responseStatus, responseSize,
                        accessTime);
            } catch (Exception e) {

            }
        }
    }
}

package com.qbao.search.engine;

import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;

/**
 * @author song.j
 * @create 2017-06-25 10:10:51
 **/
public class MonitorHandler extends SimpleHttpRequestHandler<Object> {
    @Override
    protected Object doRun() throws Exception {
        String uri = httpRequest.getUri();
        if (uri.contains("monitor.jsp")) {
            return "625";
        }

        return "url is not monitor.jsp";
    }

    @Override
    public void setServer(Server server) {

    }
}

package com.qbao.search.engine.qbaolog;

import com.qbao.search.engine.result.ResultView;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.EditResponseHttpHandler;
import com.qbao.search.util.CommonUtil;
import data.service.EsDataService;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * @author song.j
 * @create 2017-10-26 11:11:00
 **/
public class SearchHandler extends EditResponseHttpHandler<Object> {
    @Override
    public void setServer(Server server) {

    }

    @Override
    protected Object doRun() throws Exception {

        String pageId = CommonUtil.getParam(httpRequest,"pageId");

        System.out.println("pageId: " + pageId);

        return ResultView.succ(EsDataService.getInstance().searchQbaoLog(pageId));
    }

    @Override
    protected void editResponse(HttpResponse response) {
        response.setHeader("content-type","application/json;charset=utf-8");


        response.addHeader("access-control-allow-origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "OPTIONS,POST,GET");
        response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");


    }
}

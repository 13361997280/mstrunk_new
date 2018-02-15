package com.qbao.search.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qbao.search.engine.service.CompassService;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.HttpUtil;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;
import org.jboss.netty.handler.codec.http.HttpResponse;
import vo.CompassPo;

import java.util.Map;

/**
 * 指南针页面接口
 *
 * @author song.j
 * @create 2017-09-15 09:09:19
 **/
public class CompassHandler extends SimpleHttpRequestHandler<HttpResponse> {
    @Override
    protected HttpResponse doRun() throws Exception {

        Map<String, String> param = CommonUtil.getParamMap(httpRequest);

        String userId = param.get("userId");

        if (userId == null) {
            return handHttpResponse(JSON.toJSONString(CompassPo.faild("用户ID参数缺失")));
        }

        CompassService compassService = new CompassService();

        CompassPo successPo = CompassPo.success(compassService.getUserOneday(userId));

        return handHttpResponse(JSON.toJSONString(successPo, SerializerFeature.DisableCircularReferenceDetect));
    }

    @Override
    public void setServer(Server server) {
    }

    private HttpResponse handHttpResponse(String jsonString) {

        HttpResponse response = HttpUtil.getHttpResponse(jsonString, sendException, getTimeStamp());

        response.setHeader("content-type", "application/json");

        return response;
    }
}

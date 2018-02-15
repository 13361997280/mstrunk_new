package com.qbao.search.engine.qbaolog;

import com.alibaba.fastjson.JSON;
import com.qbao.search.engine.result.ResultView;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.EditResponseHttpHandler;
import com.qbao.search.util.CommonUtil;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import util.DateUtil;

import java.util.Date;
import java.util.Map;


/**
 * 日志请求处理器
 *
 * @author song.j
 * @create 2017-07-11 15:15:37
 **/
public class LogHandler extends EditResponseHttpHandler<ResultView> {

    private static ESLogger logger = Loggers.getLogger(LogHandler.class);

    @Override
    protected ResultView doRun() throws Exception {

        String method = httpRequest.getMethod().getName();
        Map paramMap = null;

        //判断请求方法类型。进行数据处理
        if (method.equals("GET")) {

            paramMap = CommonUtil.getParamMap(httpRequest);

        } else if (method.equals("POST")) {

            String jsonParam = new String(httpRequest.getContent().array());
            paramMap = JSON.parseObject(jsonParam, Map.class);

        }
        LogService logService = new LogService();

        try {
            invokeParam(paramMap);
        } catch (IllegalAccessException e) {
            logger.info(e.getMessage());
            return ResultView.fail(e.getMessage(), HttpResponseStatus.BAD_REQUEST);
        }

        handlParam(paramMap);

        logService.saveLog(paramMap);

        return ResultView.succ();
    }

    @Override
    protected void editResponse(HttpResponse response) {
        response.setHeader("Content-Type", "application/json; charset=UTF-8");

        //支持跨域请求
        response.addHeader("access-control-allow-origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "OPTIONS,POST,GET");
        response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
    }

    /**
     * 参数校检
     *
     * @throws IllegalAccessException 异常参数返回
     */
    public void invokeParam(Map paramMap) throws IllegalAccessException {


        Object[] params = paramMap.keySet().toArray();

        int required = 0;
        for (Object paramKey : params) {
            if (!QbaoLogDto.elmData.contains(paramKey)) {
                throw new IllegalAccessException("无效的参数:" + paramKey.toString());
            }

            if (QbaoLogDto.requiredData.contains(paramKey)) {
                required++;
            }
        }

        if (required < QbaoLogDto.requiredData.size()) {
            throw new IllegalAccessException("必填参数缺失");
        }

    }

    public void handlParam(Map paramMap) {
        //手转转换时间
        if (paramMap != null && paramMap.get("stamp") != null) {
            try {
                paramMap.put("stampDate", DateUtil.formatDate(new Date(Long.valueOf(paramMap.get("stamp").toString())),
                        "yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @Override
    public void setServer(Server server) {

    }
}

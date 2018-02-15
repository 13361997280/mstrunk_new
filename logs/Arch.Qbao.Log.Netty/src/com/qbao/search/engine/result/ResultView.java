package com.qbao.search.engine.result;

import com.alibaba.fastjson.JSON;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author song.j
 * @create 2017-08-16 15:15:06
 **/
public class ResultView {
    private boolean success;
    private String message;
    private int responseCode;
    private String responseDesc;
    private Object data;


    public ResultView(boolean success, String message, HttpResponseStatus resultCode, Object data) {
        this.success = success;
        this.message = message;
        this.responseCode = resultCode.getCode();
        this.responseDesc = resultCode.getReasonPhrase();
        this.data = data;
    }

    public static ResultView fail(String message, HttpResponseStatus resultCode) {
        return new ResultView(false, message, resultCode, null);
    }

    public static ResultView succ(Object data) {
        return new ResultView(true, null, HttpResponseStatus.OK, data);
    }

    public static ResultView succ() {
        return succ(null);
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

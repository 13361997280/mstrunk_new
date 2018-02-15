package com.qbao.search.engine;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

/**
 * @author by zhangchanghong on 15/12/8.
 */
public class AjaxResult {
    private static final String SUC_MSG = "ok";
    private boolean success;
    private int returnCode;
    private String message;
    private Object data;

    public AjaxResult(boolean success, int returnCode, String message, Object data) {
        this.success = success;
        this.returnCode = returnCode;
        this.message = message;
        this.data = data;
    }

    public static AjaxResult failed() {
        return new AjaxResult(false, 0, StringUtils.EMPTY, null);
    }

    public static AjaxResult failed(String message){
        return new AjaxResult(false, -2, message, null);
    }

    public static AjaxResult failed(int returnCode, String message, Object data) {
        return new AjaxResult(false, returnCode, message, data);
    }

    public static AjaxResult failed(int returnCode, String message){
        return new AjaxResult(false, returnCode, message, null);
    }

    public static AjaxResult success(){
        return new AjaxResult(true, 0, SUC_MSG, null);
    }

    public static AjaxResult success(Object data){
        return new AjaxResult(true, 0, SUC_MSG, data);
    }

//    public static AjaxResult success(String message){
//        return new AjaxResult(true, 0, message, null);
//    }

    public static AjaxResult success(String message, Object data){
        return new AjaxResult(false, 0, message, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

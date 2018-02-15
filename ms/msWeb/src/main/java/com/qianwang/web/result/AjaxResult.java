package com.qianwang.web.result;

public class AjaxResult {

    /**
     * 返回结果成功标识
     */
    private boolean success;

    private String message;

    /**
     * 返回信息主体数据
     */
    private Object data;

    public AjaxResult(String message) {
        this.success = false;
        this.message = message;
    }

    public AjaxResult(Object data) {
        this.success = true;
        this.data = data;
    }

    public AjaxResult(boolean success) {
        this.success = success;
    }

    public static AjaxResult failureResult(String message) {
        return new AjaxResult(message);
    }

    public static AjaxResult successResult(Object data) {
        return new AjaxResult(data);
    }

    public static AjaxResult success() {
        return new AjaxResult(true);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

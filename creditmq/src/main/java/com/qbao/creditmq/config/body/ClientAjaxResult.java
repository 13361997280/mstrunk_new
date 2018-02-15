package com.qbao.creditmq.config.body;

/**
 * 客户端报文
 */
public class ClientAjaxResult {
    private Boolean success = true;
    private Integer returnCode  ;       // 返回码 1000  成功 or  1005 失败
    private String message;
    private Object data;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
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


    private ClientAjaxResult(Boolean success, Integer returnCode, String message, Object data) {
        this.success = success;
        this.returnCode = returnCode;
        this.message = message;
        this.data = data;

    }


    public static final ClientAjaxResult failed(String message, Object data) {
        return new ClientAjaxResult(false, 1005, message, data);
    }


    public static final ClientAjaxResult success(String message, Object data) {
        return new ClientAjaxResult(true, 1000, message, data);
    }


}

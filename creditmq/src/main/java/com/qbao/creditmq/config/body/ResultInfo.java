package com.qbao.creditmq.config.body;

/**
 * Created by dn0879 on 16/5/30.
 */
public class ResultInfo {

    /**
     * 返回结果成功标识
     */
    private boolean success;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 返回信息主体数据
     */

    private Object data;
}

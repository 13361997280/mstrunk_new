package com.qianwang.constant;

/**
 * Created by fun
 * on 2017/11/17.
 */
public enum StatusEnum {

    ONLINE(0, "上架中"), OFFLINE(1, "已下架");
    public final Integer code;
    public final String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

package com.qianwang.dao.aspect;

/**
 * 主从库枚举
 * Created by zhangchanghong on 16/5/25.
 */
public enum DbMsEnum {
    M("master"),
    S("slave");

    private final String s;

    public String get(){return this.s;}

    DbMsEnum(String s){
        this.s = s;
    }
}

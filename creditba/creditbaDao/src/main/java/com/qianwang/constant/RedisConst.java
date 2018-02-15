package com.qianwang.constant;

/**
 *
 */
public enum RedisConst {
    CREDIT_CONF_BASE_SET("credit:conf:base:set", "信用分基础设置",  60 * 60 * 24 * 30),
    CREDIT_CONF_BUS("credit:conf:bus", "业务参数设置", 60 * 60 * 24 * 30),
    CREDIT_CONF_ITEM("credit:conf:item", "行为系数加分项", 60 * 60 * 24 * 30),
    CREDIT_CONF_RATIO("credit:conf:ratio", "行为系数收益权重",  60 * 60 * 24 * 30),
    CREDIT_CONF_SIGN("credit:conf:sign", "签到配置",  60 * 60 * 24 * 30),
    CREDIT_SYS_CONFIG("credit:conf:sys:%s", "系统属性配置",  60 * 60 * 24 * 30),
    CREDIT_CONF_TASK("credit:conf:task:%s", "任务配置",  60 * 60 * 24 * 30),
    CREDIT_CONF_TASK_SET("credit:conf:task:set", "任务基础配置",  60 * 60 * 24 * 30),

    CREDIT_CONF_RATIO_USER("credit:conf:ratio:user:%s", "用户行为系数详情",  60 * 60 * 24 * 30),
    CREDIT_CONF_ITEM_FLAG("credit:conf:item:flag:%s", "行为系数加分项-带签到状态", 60 * 60 * 24 * 30),

    Defaut("credit:message","消息管理",60 * 60 * 24 * 30);//30天

    public final String key;
    public final String desc;
    public final int expired;

    RedisConst(String key, String desc, int expired) {
        this.key = key;
        this.desc = desc;
        this.expired = expired;
    }
}

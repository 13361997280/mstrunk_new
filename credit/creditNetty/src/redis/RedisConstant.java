package redis;

/**
 * @author song.j
 * @create 2017-11-16 09:09:37
 **/
public enum  RedisConstant {


    CREDIT_CONF_BASE_SET("credit:conf:base:set", "信用分基础设置",  60 * 60 * 24 * 30),
    CREDIT_CONF_BUS("credit:conf:bus:%s", "业务参数设置", 60 * 60 * 24 * 30),
    CREDIT_CONF_ITEM("credit:conf:item", "行为系数加分项", 60 * 60 * 24 * 30),
    CREDIT_CONF_RATIO("credit:conf:ratio", "行为系数收益权重",  60 * 60 * 24 * 30),
    CREDIT_CONF_SIGN("credit:conf:sign", "签到配置",  60 * 60 * 24 * 30),
    CREDIT_SYS_CONFIG("credit:conf:sys:%s", "系统属性配置",  60 * 60 * 24 * 30),
    CREDIT_CONF_TASK("credit:conf:task:%s", "任务配置",  60 * 60 * 24 * 30),
    CREDIT_CONF_TASK_SET("credit:conf:task:set", "任务基础配置",  60 * 60 * 24 * 30),
    // 业务使用的rediskey
    CREDIT_CONF_SIGN_FLAG("credit:conf:sign:flag:%s", "用户今天是否签到",  60 * 60 * 24 * 30),
    CREDIT_CONF_TASK_FLAG("credit:conf:task:flag:%s", "任务是否存在",  60 * 60 * 24 * 30),
    CREDIT_CONF_RATIO_USER("credit:conf:ratio:user:%s", "用户行为系数详情",  60 * 60 * 24 * 30),
    CREDIT_TOTAL_SCORE_USER("credit:total:score:user:%s", "用户获得的信用总分",  60 * 60 * 24 * 30),
    CREDIT_CONF_ITEM_FLAG("credit:conf:item:flag:%s", "行为系数加分项-带签到状态", 60 * 60 * 24 * 30);


    RedisConstant(String key, String dec, int expired) {
        this.key = key;
        this.dec = dec;
        this.expired = expired;
    }

    public String key;
    public String dec;
    public int expired;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }
}

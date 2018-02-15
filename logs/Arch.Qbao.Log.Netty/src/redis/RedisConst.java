package redis;

/**
 * @author by tangtaiquan on 16/3/18.
 */
public enum RedisConst {

    
    TENGRONG_MS_TOKEN("tengrong:ms:token:%s", "数据画像开放平台token", 60 * 60 * 24),
    
    TENGRONG_MS_TOKEN_TIMES("tengrong:ms:token:times:%s", "数据画像开放平台token单日访问次数", 60 * 60 * 24),

    TEGNRONG_ONEDAY_QBAOLOG_QUEUE("tengrong:oneday:qbaolog:queue", "日志队列", 60 * 60 * 24);


    public final String key;
    public final String desc;
    public final int expired;

    RedisConst(String key, String desc, int expired) {
        this.key = key;
        this.desc = desc;
        this.expired = expired;
    }
}

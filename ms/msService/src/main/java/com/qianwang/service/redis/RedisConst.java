package com.qianwang.service.redis;

/**
 * @author by tangtaiquan on 16/3/18.
 */
public enum RedisConst {

    // 微信公众号信息缓存
    MS_WECHAT_ACCESS_TOKEN("ms_wechat_access_token", "微信token", 7000),
    MS_WECHAT_JSAPI_TICKET("ms_wechat_jsapi_ticket", "微信ticket", 7000),
    MS_WECHAT_TIMESTAMP("ms_wechat_timestamp", "微信签名时间搓", 7000),
    MS_PROVINCE_CODE("ms_province_code", "省", 86400),
    MS_CITY_CODE("ms_city_code", "市", 86400),
    MS_AREA_CODE("ms_area_code", "区", 86400),
    MS_ACTIVITY_USER_COUNT("ms_activity_user_count", "首页活跃量", 86400),
    MS_INDEX_ACTIVITY_INFO("ms_index_activity_info", "首页动态信息", 86400);

    public final String key;
    public final String desc;
    public final int expired;

    RedisConst(String key, String desc, int expired) {
        this.key = key;
        this.desc = desc;
        this.expired = expired;
    }
}

package util;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Mail: remindxiao@gmail.com <br>
 * Date: 2014-01-17 5:09 PM  <br>
 * Author: xiao
 */
public class Signatures {


    public static String sign(String toVerify, String restKey) {
        String expect = MD5Utils.md5Str(restKey,toVerify);
        return expect;
    }



    /**
     * 验证签名
     * @param signMap   请求
     * @param signKey   密钥
     * @return  校验通过
     */
    public static boolean verify(Map<String, String> signMap,  String signKey) {
        checkNotNull(signKey, "channel doesn't exists!");

        String sign = signMap.get("sign").toString();
        Map<String, String> params = Maps.newLinkedHashMap();
        for (String key : signMap.keySet()) {
            String value = signMap.get(key);
            if (isValueEmptyOrSignRelatedKey(key, value)) {
                continue;
            }
            params.put(key, value);
        }


        String toVerify = Joiner.on('&').withKeyValueSeparator("=").join(params);
        System.out.println(toVerify);
        String expect = MD5Utils.md5Str(signKey,toVerify);
        return Objects.equal(expect, sign);
    }

    private static boolean isValueEmptyOrSignRelatedKey(String key, String value) {
        return Strings.isNullOrEmpty(value) || StringUtils.equalsIgnoreCase(key, "sign")
                || StringUtils.equalsIgnoreCase(key, "sign_type");
    }
    public static void main(String[] args){
        String restKey = "657y9b7faa6a50a4abb5e00b5117ca45";
        Map<String,String> map = new LinkedHashMap<String,String>();
        map.put("productType","签到");
        map.put("userId","1");
        map.put("timestamp","1511168560335");
        map.put("sign","359120e28ee894186fbdc2caa65b8e21");
        System.out.println(sign("a=1&b=2&c=3",restKey));
        System.out.println(verify(map,restKey));
    }

}

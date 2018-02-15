package com.qianwang.web.controller;

import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songjie on 16/10/11.
 */
@RequestMapping("/qiniu")
@Controller
public class QiniuController extends BaseController {

    private final static String ACCESS_KEY = "K0qtnxHFdJTp9Wjy3Gi9dyWQIZfqZR4Ow__F724L";
    private final static String SECRET_KEY = "TqJyenJSN8T4N4Er41Kei4G2pULzQJSbZVZjXTzF";
    private final static String BUCKET_NAME = "qbmessage";

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getToken")
    @ResponseBody
    public Map getToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);


        Map tokenMap = new HashMap();
        tokenMap.put("uptoken",auth.uploadToken(BUCKET_NAME));
        return tokenMap;
    }
}

package com.qianwang.web.controller;

import com.alibaba.fastjson.JSON;
import com.qianwang.web.result.AjaxResult;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @RequestMapping(value = "/getToken")
    @ResponseBody
    public Map getToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Map tokenMap = new HashMap();
        tokenMap.put("uptoken", auth.uploadToken(BUCKET_NAME));
        return tokenMap;
    }

    @RequestMapping("upload")
    @ResponseBody
    public AjaxResult uploadImage(MultipartFile file) {

        if (file == null){
            return AjaxResult.failureResult("文件不存在");
        }

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET_NAME);

        UploadManager uploadManager = new UploadManager();

        try {
            Response response = uploadManager.put(file.getBytes(), null, upToken);

            Map respMap = JSON.parseObject(response.bodyString(), Map.class);


            String url = "https://qn-message.qbcdn.com/" + respMap.get("key");

            return AjaxResult.successResult(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return AjaxResult.failureResult("上传失败");
    }
}

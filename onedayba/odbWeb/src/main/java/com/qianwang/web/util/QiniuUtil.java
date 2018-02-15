package com.qianwang.web.util;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


/**
 * Created by songjie on 16/10/31.
 */
public class QiniuUtil {
    private final static Logger LOG = LoggerFactory.getLogger(QiniuUtil.class);

    public final static String ACCESS_KEY = "K0qtnxHFdJTp9Wjy3Gi9dyWQIZfqZR4Ow__F724L";
    public final static String SECRET_KEY = "TqJyenJSN8T4N4Er41Kei4G2pULzQJSbZVZjXTzF";

    public final static String BUCKET_NAME = "qbmessage";

    public final static String FILE_PATH = "https://7sbrcg.com1.z0.glb.clouddn.com/";

    /**
     * 图片上传至7牛
     *
     * @param byteOrFile
     * @return
     */
    public static String upload(byte[] byteOrFile) {

        //获取七牛上传附件的token
        Auth auth = Auth.create(QiniuUtil.ACCESS_KEY, QiniuUtil.SECRET_KEY);
        String token = auth.uploadToken(BUCKET_NAME);

        UploadManager uploadManager = new UploadManager();

        Response response;
        try {
            String  key = UUID.randomUUID().toString();

            response = uploadManager.put(byteOrFile, key, token);

            String fileName = response.jsonToMap().get("key").toString();

            return FILE_PATH + fileName;

        } catch (Exception e) {
            LOG.error("QiniuUtil upload error", e.getMessage());

        }

        return null;
    }

}

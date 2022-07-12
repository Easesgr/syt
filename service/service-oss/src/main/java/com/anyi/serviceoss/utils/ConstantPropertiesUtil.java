package com.anyi.serviceoss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;

/**
 * @author 安逸i
 * @version 1.0
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String key;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;


    public static String  ENDPOINT;
    public static String  KEY;
    public static String  KEY_SECRET;
    public static String  BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        KEY = key;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}

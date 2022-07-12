package com.anyi.sms.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 安逸i
 * @version 1.0
 */
@Getter
@Setter
@Component
//注意prefix要写到最后一个 "." 符号之前
//调用setter为成员赋值
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsField implements InitializingBean {
    private String sdkAppId;
    private String secretId;
    private String secretKey;
    private String templateId;
    private String signName;

    public static String SDK_ID;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String TEMPLATE_CODE;
    public static String SIGN_NAME;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        SDK_ID = sdkAppId;
        KEY_ID = secretId;
        KEY_SECRET = secretKey;
        TEMPLATE_CODE = templateId;
        SIGN_NAME = signName;
    }
}

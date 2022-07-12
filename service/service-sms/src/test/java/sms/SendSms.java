package sms;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
/**
 * @author 安逸i
 * @version 1.0
 */
public class SendSms
{
    public static void main( String[] args )
    {
        try {
            Credential cred = new Credential("AKIDa0B7nEQM3UgfPu99tOddNs77rMbbMr66", "mKdgU2LOBj14sQilY5NPBiz1nKbdPva0");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            String appid = "1400684835";
            req.setSmsSdkAppid(appid);

            String sign = "安逸i个人网站";
            req.setSign(sign);

            /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
            String senderid = "";
            req.setSenderId(senderid);

            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
            String session = "";
            req.setSessionContext(session);

            /* 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper] */
            String extendcode = "";
            req.setExtendCode(extendcode);

            String templateID = "1416184";
            req.setTemplateID(templateID);

            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+8613699559281"};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空*/
            String[] templateParams = {"1348"};
            req.setTemplateParamSet(templateParams);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);

            // 输出 JSON 格式的字符串回包
            System.out.println();
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
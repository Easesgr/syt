package sms;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

// 导入对应SMS模块的client
import com.tencentcloudapi.sms.v20210111.SmsClient;

// 导入要请求接口对应的request response类
import com.tencentcloudapi.sms.v20210111.models.PullSmsReplyStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsReplyStatusResponse;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;

public class PullSmsSendStatus {
    public static void main(String[] args) {
        try {
            Credential cred = new Credential("AKIDa0B7nEQM3UgfPu99tOddNs77rMbbMr66", "mKdgU2LOBj14sQilY5NPBiz1nKbdPva0");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();

            String sdkAppId = "1400684835";
            req.setSmsSdkAppId(sdkAppId);

            Long limit = 5L;
            req.setLimit(limit);

            PullSmsSendStatusResponse res = client.PullSmsSendStatus(req);

            System.out.println(PullSmsSendStatusResponse.toJsonString(res));

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
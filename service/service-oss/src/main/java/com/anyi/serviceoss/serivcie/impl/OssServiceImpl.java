package com.anyi.serviceoss.serivcie.impl;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.anyi.serviceoss.serivcie.OssService;
import com.anyi.serviceoss.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFile(MultipartFile file,String module) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.ENDPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtil.KEY;
        String accessKeySecret = ConstantPropertiesUtil.KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        String original = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();

        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        fileName =module+"/"+ datetime + "/" +fileName + original;
        String uploadUrl ="";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream =file.getInputStream();
            // 创建PutObject请求。
            ossClient.putObject(bucketName, fileName, inputStream);


            // https://edu-teacher-120.oss-cn-hangzhou.aliyuncs.com/1.jpg
            uploadUrl = "http://" + bucketName + "." + endpoint + "/" + fileName;

        } catch (Exception e) {
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            return uploadUrl;
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                ConstantPropertiesUtil.ENDPOINT,
                ConstantPropertiesUtil.KEY,
                ConstantPropertiesUtil.KEY_SECRET);

        // https://srb-file-200921.oss-cn-beijing.aliyuncs.com/
        // test/2021/02/27/f1673221-efb4-4356-98f4-9f0595caa927.jpg
        String host = "https://" + ConstantPropertiesUtil.BUCKET_NAME + "." + ConstantPropertiesUtil.ENDPOINT + "/";
        String objectName = fileName.substring(host.length());

        try {
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(ConstantPropertiesUtil.BUCKET_NAME, objectName);
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        // 关闭OSSClient。
        ossClient.shutdown();
        return true;
    }
}

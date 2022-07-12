package com.anyi.serviceoss.serivcie;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface OssService {
    String  uploadFile(MultipartFile file,String module);
    public boolean deleteFile(String fileName);
}

package com.anyi.yygh.client;

import com.anyi.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 安逸i
 * @version 1.0
 */
@FeignClient("service-sms")
@Repository
public interface SmsFeignClient {
    @GetMapping("/api/sms/send/{phone}")
    public Result sendCode(@PathVariable String phone);
}
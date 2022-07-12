package com.anyi.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 安逸i
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.anyi")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.anyi")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
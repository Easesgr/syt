package com.anyi.serviceoss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 安逸i
 * @version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.anyi")
public class ServiceOssMain {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssMain.class, args);
    }
}

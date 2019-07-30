package com.xuecheng.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain") //扫面实体类
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common包下的类
public class CmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsClientApplication.class, args);
    }
}

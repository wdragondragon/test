package com.jdragon.springboot;


import com.jdragon.springboot.commons.zFeign.ZFeignScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:17
 * @Description:
 */
@SpringBootApplication
@EnableScheduling
@ZFeignScan("com.jdragon.springboot")
@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
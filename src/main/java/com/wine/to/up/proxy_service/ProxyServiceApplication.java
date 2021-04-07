package com.wine.to.up.proxy_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author : bgubanov
 * @since : 21.03.2021,  вс
 **/
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EnableSwagger2
public class ProxyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyServiceApplication.class, args);
    }
}
//ping jenkins

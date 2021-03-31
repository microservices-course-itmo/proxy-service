package com.wine.to.up.proxy_service.configuration;

import com.wine.to.up.proxy_service.service.ProxyClient;
import com.wine.to.up.proxy_service.service.impl.ProxyClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean
    public ProxyClient proxyClient() {
        return new ProxyClientImpl();
    }
}

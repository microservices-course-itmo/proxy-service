package com.wine.to.up.proxy_service.configuration;

import com.wine.to.up.proxy_service.job.GetProxiesJob;
import com.wine.to.up.proxy_service.service.ProxyClient;
import com.wine.to.up.proxy_service.service.ProxyService;
import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import com.wine.to.up.proxy_service.service.impl.ProxyClientImpl;
import com.wine.to.up.proxy_service.service.impl.ProxyServiceImpl;
import com.wine.to.up.proxy_service.service.impl.ProxyValidatorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean
    public ProxyClient proxyClient() {
        return new ProxyClientImpl();
    }

    @Bean
    public ProxyValidatorService proxyValidatorService() {
        return new ProxyValidatorServiceImpl();
    }

    @Bean
    public ProxyService proxyService(ProxyClient proxyClient, ProxyValidatorService proxyValidatorService) {
        return new ProxyServiceImpl(proxyClient, proxyValidatorService);
    }
}

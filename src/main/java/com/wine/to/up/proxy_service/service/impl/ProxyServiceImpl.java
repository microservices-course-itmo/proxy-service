package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.service.ProxyClient;
import com.wine.to.up.proxy_service.service.ProxyService;
import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private final ProxyClient proxyClient;
    private final ProxyValidatorService proxyValidatorService;

    public ProxyServiceImpl(ProxyClient proxyClient, ProxyValidatorService proxyValidatorService) {
        this.proxyClient = proxyClient;
        this.proxyValidatorService = proxyValidatorService;
    }

    @Override
    public void getProxies() {
        log.info("Я тута -> ProxyServiceImpl");
    }
}

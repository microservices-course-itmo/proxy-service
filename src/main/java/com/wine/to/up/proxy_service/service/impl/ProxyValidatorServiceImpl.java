package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import com.wine.to.up.proxy_service.utils.ProxyDto;
import org.springframework.beans.factory.annotation.Value;

import java.net.Proxy;
import java.util.List;


public class ProxyValidatorServiceImpl implements ProxyValidatorService {

    @Value(value = "${default.url.for.ping}")
    private String defaultUrl;

    @Override
    public List<ProxyDto> validateProxy(List<Proxy> proxyList) {
        return validateProxy(proxyList, defaultUrl);
    }

    @Override
    public List<ProxyDto> validateProxy(List<Proxy> proxyList, String url) {
        return null;
    }
}

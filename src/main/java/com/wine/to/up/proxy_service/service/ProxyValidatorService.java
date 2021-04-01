package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.utils.ProxyDto;

import java.net.Proxy;
import java.util.List;

public interface ProxyValidatorService {

    boolean isProxyAlive(Proxy proxy);

    long pingUrlWithProxy(String url, com.wine.to.up.proxy_service.entity.Proxy proxy);
}

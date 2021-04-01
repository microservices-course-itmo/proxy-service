package com.wine.to.up.proxy_service.service;

import java.net.Proxy;

public interface ProxyValidatorService {

    boolean isProxyAlive(Proxy proxy);

    long pingUrlWithProxy(String url, com.wine.to.up.proxy_service.entity.Proxy proxy);
}

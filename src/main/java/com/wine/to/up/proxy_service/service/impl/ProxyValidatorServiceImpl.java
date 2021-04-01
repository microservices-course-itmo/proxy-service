package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;


public class ProxyValidatorServiceImpl implements ProxyValidatorService {

    @Value(value = "${default.url.for.ping}")
    private String defaultUrl;

    @Override
    public boolean isProxyAlive(Proxy proxy) {
        try {
            Jsoup.connect(defaultUrl).proxy(proxy).timeout(10000).get();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public long pingUrlWithProxy(String url, Proxy proxy) {
        try {
            long timeStart = System.currentTimeMillis();
            Jsoup.connect(url).proxy(proxy).timeout(10000).get();
            long timeEnd = System.currentTimeMillis();
            return timeEnd - timeStart;
        } catch (IOException e) {
            return -1;
        }
    }

}

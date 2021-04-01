package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;
import com.wine.to.up.proxy_service.service.ProxyRestService;
import com.wine.to.up.proxy_service.service.ProxyService;

import java.util.List;

/**
 * @author : skorz
 * @since : 01.04.2021, чт
 **/
public class ProxyRestServiceImpl implements ProxyRestService {

    private final ProxyService proxyService;

    public ProxyRestServiceImpl(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Override
    public List<ParserProxy> getProxies(Parser parser) {
        return proxyService.getProxies(parser);
    }

    @Override
    public void cleanDatabase() {

    }
}

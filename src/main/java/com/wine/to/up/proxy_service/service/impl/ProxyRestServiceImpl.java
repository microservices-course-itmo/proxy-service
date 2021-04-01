package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.ParserProxies;
import com.wine.to.up.proxy_service.service.ProxyRestService;

import java.util.ArrayList;
import java.util.List;

public class ProxyRestServiceImpl implements ProxyRestService {
    @Override
    public void cleanDatabase() {
        return;
    }

    @Override
    public List<ParserProxies> getProxies(String serviceName) {
        return new ArrayList<>();
    }
}

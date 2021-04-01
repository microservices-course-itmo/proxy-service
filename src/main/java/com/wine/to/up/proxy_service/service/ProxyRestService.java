package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.entity.ParserProxies;

import java.util.List;

/**
 * @author : bgubanov
 * @since : 01.04.2021
 **/

public interface ProxyRestService {

    void cleanDatabase();

    List<ParserProxies> getProxies(String serviceName);
}

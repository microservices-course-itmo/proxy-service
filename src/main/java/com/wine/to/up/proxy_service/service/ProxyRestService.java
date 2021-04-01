package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;

import java.util.List;

/**
 * @author : bgubanov
 * @since : 01.04.2021
 **/

public interface ProxyRestService {

    void cleanDatabase();

    List<ParserProxy> getProxies(Parser parser);
}

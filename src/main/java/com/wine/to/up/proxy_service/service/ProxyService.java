package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;

import java.util.List;

public interface ProxyService {

    void updateProxies();

    void updateParserProxies();

    List<ParserProxy> getProxies(Parser parser);
}

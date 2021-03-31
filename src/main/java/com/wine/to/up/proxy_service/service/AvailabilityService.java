package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.Proxy;

import java.util.concurrent.TimeoutException;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

public interface AvailabilityService {
    long measurePingParserWithProxy(Parser parser, Proxy proxy) throws TimeoutException;
}

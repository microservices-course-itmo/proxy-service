package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.Proxy;
import com.wine.to.up.proxy_service.service.AvailabilityService;

import java.util.concurrent.TimeoutException;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

public class AvailabilityServiceImpl implements AvailabilityService {

    @Override
    public long measurePingParserWithProxy(Parser parser, Proxy proxy) throws TimeoutException {
        return 0;
    }
}

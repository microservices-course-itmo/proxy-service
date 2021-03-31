package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.service.AvailabilityService;

import java.util.concurrent.TimeoutException;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

public class AvailabilityServiceImpl implements AvailabilityService {
    @Override
    public long measurePingParser(Parser parser) throws TimeoutException {
        return 0;
    }
}

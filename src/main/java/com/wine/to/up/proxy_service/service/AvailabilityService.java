package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.entity.Parser;

import java.util.concurrent.TimeoutException;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

public interface AvailabilityService {
    long measurePingParser(Parser parser) throws TimeoutException;
}

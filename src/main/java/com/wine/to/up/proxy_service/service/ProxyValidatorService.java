package com.wine.to.up.proxy_service.service;

import com.wine.to.up.proxy_service.utils.ProxyDto;

import java.net.Proxy;
import java.util.List;

public interface ProxyValidatorService {

    List<ProxyDto> validateProxy(List<Proxy> proxyList);

    List<ProxyDto> validateProxy(List<Proxy> proxyList, String url);
}

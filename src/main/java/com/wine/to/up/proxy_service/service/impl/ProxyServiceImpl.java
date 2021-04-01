package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxies;
import com.wine.to.up.proxy_service.repository.ParserProxiesRepository;
import com.wine.to.up.proxy_service.repository.ProxyRepository;
import com.wine.to.up.proxy_service.service.ProxyClient;
import com.wine.to.up.proxy_service.service.ProxyService;
import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private final ProxyClient proxyClient;
    private final ProxyValidatorService proxyValidatorService;

    private final ProxyRepository proxyRepository;
    private final ParserProxiesRepository parserProxiesRepository;

    public ProxyServiceImpl(ProxyClient proxyClient,
                            ProxyValidatorService proxyValidatorService,
                            ProxyRepository proxyRepository,
                            ParserProxiesRepository parserProxiesRepository) {
        this.proxyClient = proxyClient;
        this.proxyValidatorService = proxyValidatorService;
        this.proxyRepository = proxyRepository;
        this.parserProxiesRepository = parserProxiesRepository;
    }

    @Override
    public void getProxies() {
        log.info("Я тута -> ProxyServiceImpl");
        List<com.wine.to.up.proxy_service.entity.Proxy> proxyList = new ArrayList<>();
        List<Proxy> list = proxyClient.getProxyList();
        log.info("Начинаю пинговать гугл и проверять на наличие в бд");
        list.removeIf(proxy -> {
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            return !proxyValidatorService.isProxyAlive(proxy) || proxyRepository.existsByIpAndPort(socketAddress.getHostName(), socketAddress.getPort());
        });
        log.info("И тута после удаления осталось {} прокси", list.size());
        list.forEach(proxy ->
                proxyList.add(new com.wine.to.up.proxy_service.entity.Proxy(proxy)));
        log.info("Сохранил прокси в бд, начинаю пинговать всё остальное");
        for (com.wine.to.up.proxy_service.entity.Proxy proxy : proxyList) {
            boolean isProxyInDB = false;
            for (Parser parser : Parser.values()) {
                long ping = proxyValidatorService.pingUrlWithProxy(parser.getPath(), proxy);
                if (ping != -1) {
                    if (!isProxyInDB) {
                        proxyRepository.save(proxy);
                        isProxyInDB = true;
                    }
                    parserProxiesRepository.save(new ParserProxies(parser.name(), proxy, ping));
                }
            }
        }
    }
}

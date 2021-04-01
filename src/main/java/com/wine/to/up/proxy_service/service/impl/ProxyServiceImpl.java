package com.wine.to.up.proxy_service.service.impl;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;
import com.wine.to.up.proxy_service.repository.ParserProxiesRepository;
import com.wine.to.up.proxy_service.repository.ProxyRepository;
import com.wine.to.up.proxy_service.service.ProxyClient;
import com.wine.to.up.proxy_service.service.ProxyService;
import com.wine.to.up.proxy_service.service.ProxyValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private final ProxyClient proxyClient;
    private final ProxyValidatorService proxyValidatorService;
    private final ProxyRepository proxyRepository;
    private final ParserProxiesRepository parserProxiesRepository;

    public ProxyServiceImpl(
            ProxyClient proxyClient,
            ProxyValidatorService proxyValidatorService,
            ProxyRepository proxyRepository,
            ParserProxiesRepository parserProxiesRepository) {
        this.proxyClient = proxyClient;
        this.proxyValidatorService = proxyValidatorService;
        this.proxyRepository = proxyRepository;
        this.parserProxiesRepository = parserProxiesRepository;
    }

    @Override
    public void updateProxies() {
        List<Proxy> proxies = proxyClient.getProxyList();
        proxies.forEach(e -> {
            if (proxyValidatorService.isProxyAlive(e)) {
                InetSocketAddress address = (InetSocketAddress) e.address();
                com.wine.to.up.proxy_service.entity.Proxy proxy = new com.wine.to.up.proxy_service.entity.Proxy(address.getHostName(), address.getPort());
                if (!proxyRepository.existsByIpAndPort(proxy.getIp(), proxy.getPort())) {
                    proxyRepository.save(proxy);
                }
            }
        });

    }

    @Override
    public void updateParserProxies() {
        proxyRepository.findAll().forEach(e -> Arrays.stream(Parser.values()).forEach(p -> {
            Proxy javaProxy = e.getJavaProxy();
            long ping = proxyValidatorService.pingUrlWithProxy(p.getPath(), javaProxy);
            if (ping > 0) {
                ParserProxy proxy = new ParserProxy();
                proxy.setParser(p);
                proxy.setProxy(e);
                proxy.setPing(ping);
                if (!parserProxiesRepository.existsByProxyAndParser(e, p)) {
                    parserProxiesRepository.save(proxy);
                } else {
                    ParserProxy existProxy = parserProxiesRepository.findFirstByProxyAndParser(e, p);
                    existProxy.setPing(ping);
                    parserProxiesRepository.save(existProxy);
                }
            }
        }));
    }

    @Override
    public List<ParserProxy> getProxies(Parser parser) {
        return parserProxiesRepository.getAllByParserOrderByPingAsc(parser);
    }

    @Override
    public void cleanUselessProxies() {
        parserProxiesRepository.findAll().forEach(p -> {
            if (proxyValidatorService.pingUrlWithProxy(
                    p.getParser().getPath(),
                    p.getProxy().getJavaProxy()
            ) < 0) {
                parserProxiesRepository.delete(p);
            }
        });
        proxyRepository.findAll().forEach(p -> {
            if (!parserProxiesRepository.existsByProxy(p)) {
                proxyRepository.delete(p);
            }
        });
    }
}

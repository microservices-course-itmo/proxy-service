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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private static final long HOUR = 1800000;

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
        ForkJoinPool proxyThreadPool = null;
        try {
            proxyThreadPool = new ForkJoinPool(10);
            proxyThreadPool.submit(() -> proxies.parallelStream().forEach(e -> {
                if (proxyValidatorService.isProxyAlive(e)) {
                    InetSocketAddress address = (InetSocketAddress) e.address();
                    com.wine.to.up.proxy_service.entity.Proxy proxy = new com.wine.to.up.proxy_service.entity.Proxy(address.getHostName(), address.getPort());
                    if (!proxyRepository.existsByIpAndPort(proxy.getIp(), proxy.getPort())) {
                        proxyRepository.save(proxy);
                    }
                }
            })).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (proxyThreadPool != null) {
                proxyThreadPool.shutdown();
            }
        }
    }

    @Override
    public void deleteProxies() {
        List<com.wine.to.up.proxy_service.entity.Proxy> proxyList =
                proxyRepository.getAllByCreateDateIsLessThan(new Date(new Date().getTime() - HOUR));
        log.info("Получил список проксей, старее получаса - {}", proxyList.size());
        List<ParserProxy> parserProxiesList =
                parserProxiesRepository.findAllByProxyIn(proxyList);
        List<ParserProxy> parserProxiesList4Delete = new ArrayList<>();
        log.info("Получил список проксей парсеров, старее получаса - {}", parserProxiesList.size());
        for (ParserProxy parserProxy : parserProxiesList) {
            long ping = proxyValidatorService.pingUrlWithProxy(Parser.valueOf(parserProxy.getParserName()).getPath(), parserProxy.getProxy());
            if (ping == -1) {
                log.info("Удалил проксю парсера, id = {}", parserProxy.getId());
                parserProxiesList4Delete.add(parserProxy);
                parserProxiesRepository.delete(parserProxy);
            } else {
                parserProxy.setPing(ping);
                parserProxiesRepository.save(parserProxy);
                log.info("Обновил информацию по проксе парсера, id = {}", parserProxy.getId());
            }
        }
        parserProxiesList.removeAll(parserProxiesList4Delete);
        for (com.wine.to.up.proxy_service.entity.Proxy proxy : proxyList) {
            boolean isProxyActive = false;
            for (ParserProxy parserProxy : parserProxiesList) {
                if (parserProxy.getProxy().getId().equals(proxy.getId())) {
                    isProxyActive = true;
                }
            }
            if (!isProxyActive) {
                log.info("Удалил прокси, id = {}", proxy.getId());
                proxyRepository.delete(proxy);
            } else {
                proxy.setCreateDate(new Date());
                proxyRepository.save(proxy);
                log.info("Обновил прокси, id = {}", proxy.getId());
            }
        }
    }

    @Override
    public void updateParserProxies() {
        ForkJoinPool proxyThreadPool = null;
        try {
            proxyThreadPool = new ForkJoinPool(10);
            proxyThreadPool.submit(() -> proxyRepository.findAll().parallelStream().forEach(e -> {
                ForkJoinPool parserThreadPool = null;
                try {
                    parserThreadPool = new ForkJoinPool(6);
                    parserThreadPool.submit(() -> Arrays.stream(Parser.values()).parallel().forEach(p -> {
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
                    })).get();
                } catch (Exception parserException) {
                    parserException.printStackTrace();
                } finally {
                    if (parserThreadPool != null) {
                        parserThreadPool.shutdown();
                    }
                }
            })).get();
        } catch (Exception proxyException) {
            proxyException.printStackTrace();
        } finally {
            if (proxyThreadPool != null) {
                proxyThreadPool.shutdown();
            }
        }
    }

    @Override
    public List<ParserProxy> getProxies(Parser parser) {
        return parserProxiesRepository.getAllByParserOrderByPingAsc(parser);
    }

    @Override
    public void cleanUselessProxies() {
        ForkJoinPool pool = null;
        try {
            pool = new ForkJoinPool(10);
            pool.submit(() -> parserProxiesRepository.findAll().parallelStream().forEach(p -> {
                if (proxyValidatorService.pingUrlWithProxy(
                        p.getParser().getPath(),
                        p.getProxy().getJavaProxy()
                ) < 0) {
                    parserProxiesRepository.delete(p);
                }
            })).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
        proxyRepository.findAll().forEach(p -> {
            if (!parserProxiesRepository.existsByProxy(p)) {
                proxyRepository.delete(p);
            }
        });
    }
}

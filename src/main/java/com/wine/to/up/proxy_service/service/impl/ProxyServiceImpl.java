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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private static final long HOUR = 1800000;

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
        List<ParserProxies> parserProxiesList = new ArrayList<>();
        List<Proxy> list = proxyClient.getProxyList();
        log.info("Начинаю пинговать гугл и проверять на наличие в бд");
        list.removeIf(proxy -> {
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            return !proxyValidatorService.isProxyAlive(proxy) || proxyRepository.existsByIpAndPort(socketAddress.getHostName(), socketAddress.getPort());
        });
        log.info("И тута после удаления осталось {} прокси", list.size());
        list.forEach(proxy ->
                proxyList.add(new com.wine.to.up.proxy_service.entity.Proxy(proxy)));
        log.info("Начинаю проверять прокси по всем парсерам");
        List<com.wine.to.up.proxy_service.entity.Proxy> proxyList4Delete = new ArrayList<>();
        for (com.wine.to.up.proxy_service.entity.Proxy proxy : proxyList) {
            boolean isProxyInDB = false;
            for (Parser parser : Parser.values()) {
                long ping = proxyValidatorService.pingUrlWithProxy(parser.getPath(), proxy);
                if (ping != -1) {
                    isProxyInDB = true;
                    parserProxiesList.add(new ParserProxies(parser.name(), proxy, ping));
                }
            }
            if (!isProxyInDB) {
                proxyList4Delete.add(proxy);
            }
        }
        proxyList.removeAll(proxyList4Delete);

        log.info("Начинаю сохранять прокси в бд");
        Date now = new Date();
        for (com.wine.to.up.proxy_service.entity.Proxy proxy : proxyList) {
            proxy.setCreateDate(now);
            proxyRepository.save(proxy);
        }
        parserProxiesList.forEach(parserProxiesRepository::save);
    }

    @Override
    public void deleteProxies() {
        List<com.wine.to.up.proxy_service.entity.Proxy> proxyList =
                proxyRepository.getAllByCreateDateIsLessThan(new Date(new Date().getTime() - HOUR));
        log.info("Получил список проксей, старее получаса - {}", proxyList.size());
        List<ParserProxies> parserProxiesList =
                parserProxiesRepository.findAllByProxyIn(proxyList);
        List<ParserProxies> parserProxiesList4Delete = new ArrayList<>();
        log.info("Получил список проксей парсеров, старее получаса - {}", parserProxiesList.size());
        for (ParserProxies parserProxy : parserProxiesList) {
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
            for (ParserProxies parserProxy : parserProxiesList) {
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
}

package com.wine.to.up.proxy_service.controller;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxies;
import com.wine.to.up.proxy_service.service.ProxyRestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : bgubanov
 * @since : 01.04.2021
 **/

@RestController
@RequestMapping("/")
public class ProxyController {
    //get /parsers список всех парсеров
    //get /proxies?serviceName=am-parser-service список всех актуальных проксей, сортированный по увеличению времени отклика
    private ProxyRestService proxyRestService;

    public ProxyController(ProxyRestService proxyRestService) {
        this.proxyRestService = proxyRestService;
    }

    @GetMapping("/proxies")
    public List<ParserProxies> getProxies(@RequestParam String serviceName) {
        return proxyRestService.getProxies(serviceName);
    }

    @GetMapping("/parsers")
    public List<String> getParsers() {
        return Arrays.stream(Parser.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GetMapping("/clean")
    public void cleanDatabase() {
        proxyRestService.cleanDatabase();
    }
}

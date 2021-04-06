package com.wine.to.up.proxy_service.controller;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;
import com.wine.to.up.proxy_service.service.ProxyRestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
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
    //get /proxies?serviceName=AM_PARSER_SERVICE список всех актуальных проксей, сортированный по увеличению времени отклика
    @Resource
    private ProxyRestService restService;

    @GetMapping("/proxies")
    public List<ParserProxy> getProxies(@RequestParam String serviceName) {
        try {
            return restService.getProxies(Parser.valueOf(serviceName));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/parsers")
    public List<String> getParsers() {
        return Arrays.stream(Parser.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GetMapping("/clean")
    public void cleanDatabase() {
        restService.cleanDatabase();
    }
}

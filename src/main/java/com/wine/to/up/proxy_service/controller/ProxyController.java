package com.wine.to.up.proxy_service.controller;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;
import com.wine.to.up.proxy_service.entity.Proxy;
import com.wine.to.up.proxy_service.service.ProxyRestService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

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
    @ApiOperation(value = "Получение списка прокси для парсера", httpMethod = "GET", notes = "Возвращает прокси для определённого парсера, ранжированные по увеличению времени ответа")
    public List<Proxy> getProxies(
        @ApiParam(
            name = "serviceName",
            type = "String",
            value = "Название парсера",
            example = "AM_PARSER_SERVICE",
            required = true)
        @RequestParam String serviceName) {
        try {
            return restService.getProxies(Parser.valueOf(serviceName)).stream()
                    .map(ParserProxy::getProxy)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/parsers")
    @ApiOperation(value = "Получение списка всех парсеров", httpMethod = "GET", notes = "Возвращает список всех парсеров")
    public List<String> getParsers() {
        return Arrays.stream(Parser.values()).map(Enum::name).collect(Collectors.toList());
    }
    
    @GetMapping("/clean")
    @ApiOperation(value = "Очистка базы данных", httpMethod = "GET", notes = "Очищает базу данных")
    public void cleanDatabase() {
        restService.cleanDatabase();
    }
}

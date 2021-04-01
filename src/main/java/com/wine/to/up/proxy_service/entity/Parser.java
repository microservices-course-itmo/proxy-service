package com.wine.to.up.proxy_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : bgubanov
 * @since : 30.03.2021, вт
 **/
@AllArgsConstructor
public enum Parser {
    AM_PARSER_SERVICE("https://amwine.ru"),
    PARSER_SERVICE("http://perekrestok-store.ru/"),
    LENTA_PARSER_SERVICE("https://lenta.com/"),
    SIMPLE_PARSER_SERVICE("https://simplewine.ru/"),
    WINESTYLE_PARSER_SERVICE("https://spb.winestyle.ru/"),
    WINELAB_PARSER_SERVICE("https://www.winelab.ru/");

    @Getter
    private final String path;
}

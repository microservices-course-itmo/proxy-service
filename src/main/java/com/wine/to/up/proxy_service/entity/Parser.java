package com.wine.to.up.proxy_service.entity;

/**
 * @author : bgubanov
 * @since : 30.03.2021, вт
 **/
public enum Parser {
    AM_PARSER_SERVICE("am-parser-service", "https://amwine.ru"),
    PARSER_SERVICE("crossroad-parser-service", "http://perekrestok-store.ru/"),
    LENTA_PARSER_SERVICE("lenta-parser-service", "https://lenta.com/"),
    SIMPLE_PARSER_SERVICE("simple-parser-service", "https://simplewine.ru/"),
    WINESTYLE_PARSER_SERVICE("winestyle-parser-service", "https://spb.winestyle.ru/"),
    WINELAB_PARSER_SERVICE("winelab-parser-service", "https://www.winelab.ru/");

    private final String name;
    private final String path;

    Parser(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}

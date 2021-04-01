package com.wine.to.up.proxy_service.entity;

/**
 * @author : bgubanov
 * @since : 30.03.2021, вт
 **/
public enum Parser {
    AM_PARSER_SERVICE("https://amwine.ru"),
    CROSSROAD_PARSER_SERVICE("http://perekrestok-store.ru/"),
    LENTA_PARSER_SERVICE("https://lenta.com/"),
    SIMPLE_PARSER_SERVICE("https://simplewine.ru/"),
    WINESTYLE_PARSER_SERVICE("https://spb.winestyle.ru/"),
    WINELAB_PARSER_SERVICE("https://www.winelab.ru/");

    private final String path;

    Parser(String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }
}

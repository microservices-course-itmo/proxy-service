package com.wine.to.up.proxy_service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : bgubanov
 * @since : 30.03.2021, вт
 **/
@Entity
@Table(name = "parser_proxies")
public class ParserProxies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parser_name")
    private String parserName;

    private Float ping;
}

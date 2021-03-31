package com.wine.to.up.proxy_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "proxies")
@Setter
@Getter
@NoArgsConstructor
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
}

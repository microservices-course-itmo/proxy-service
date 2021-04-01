package com.wine.to.up.proxy_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author : bgubanov
 * @since : 30.03.2021, вт
 **/

@Entity
@Table(name = "parser_proxies")
@Setter
@Getter
@NoArgsConstructor
public class ParserProxies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parser_name")
    private String parserName;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Proxy proxy;
    private Long ping;

    public ParserProxies(String parserName, Proxy proxy, Long ping) {
        this.parserName = parserName;
        this.proxy = proxy;
        this.ping = ping;
    }
}

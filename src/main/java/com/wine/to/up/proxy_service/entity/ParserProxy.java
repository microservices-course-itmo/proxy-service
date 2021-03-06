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
public class ParserProxy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parser_name")
    @Enumerated(EnumType.ORDINAL)
    private Parser parser;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Proxy proxy;
    private Long ping;
}

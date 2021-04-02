package com.wine.to.up.proxy_service.repository;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxy;
import com.wine.to.up.proxy_service.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

@Repository
public interface ParserProxiesRepository extends JpaRepository<ParserProxy, Long> {

    boolean existsByProxyAndParser(Proxy proxy, Parser parser);

    ParserProxy findFirstByProxyAndParser(Proxy proxy, Parser parser);

    List<ParserProxy> getAllByParserOrderByPingAsc(Parser parser);

    boolean existsByProxy(Proxy proxy);

    List<ParserProxy> findAllByParser(Parser Parser);

    List<ParserProxy> findAllByProxyIn(List<Proxy> list);
}
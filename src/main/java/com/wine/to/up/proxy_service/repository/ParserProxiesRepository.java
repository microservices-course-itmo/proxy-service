package com.wine.to.up.proxy_service.repository;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.entity.ParserProxies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

@Repository
public interface ParserProxiesRepository extends JpaRepository<ParserProxies, Long> {
    List<ParserProxies> findAllByParser(Parser parser);
}
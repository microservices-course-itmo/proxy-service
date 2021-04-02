package com.wine.to.up.proxy_service.repository;

import com.wine.to.up.proxy_service.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author : bgubanov
 * @since : 31.03.2021
 **/

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {

    List<Proxy> getAllById(Long id);

    boolean existsByIpAndPort(String ip, Integer port);

    List<Proxy> getAllByCreateDateIsLessThan(Date now);

    boolean existsByIpAndPort(String ip, int port);

}

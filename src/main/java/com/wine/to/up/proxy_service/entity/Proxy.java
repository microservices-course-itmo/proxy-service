package com.wine.to.up.proxy_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.net.InetSocketAddress;

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

    private String ip;
    private Integer port;

    public Proxy(java.net.Proxy proxy) {
        InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
        this.ip = socketAddress.getHostName();
        this.port = socketAddress.getPort();
    }

    public Proxy(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public java.net.Proxy getJavaProxy() {
        return new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }
}

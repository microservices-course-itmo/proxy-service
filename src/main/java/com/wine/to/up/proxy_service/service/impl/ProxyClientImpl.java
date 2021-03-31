package com.wine.to.up.proxy_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.proxy_service.service.ProxyClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProxyClientImpl implements ProxyClient {

    @Value("${proxy.api-url}")
    private String apiUrl;

    @Value("${proxy.api-key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Proxy> getProxyList() {
        HttpUrl.Builder builder = HttpUrl.parse(apiUrl).newBuilder();
        builder.addQueryParameter("key", apiKey);
        builder.addQueryParameter("type", "1");
        Request request = new Request.Builder().url(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() < 200 || response.code() > 204 || response.body() == null) {
                return Collections.emptyList();
            }
            ProxyResponse proxyResponse = mapper.readValue(response.body().bytes(), ProxyResponse.class);
            List<Proxy> proxyList = proxyResponse.data.stream()
                    .map(proxy -> new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(proxy.ip, proxy.port)))
                    .collect(Collectors.toList());
            if(!proxyList.isEmpty()) {
                log.info("Valid proxy count : {}", proxyList.size());
                return proxyList;
            } else {
                log.info("Trying again with proxy");
                return getProxyList();
            }
        } catch (IOException e) {
            log.info("Cannot retrieve proxies: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class ProxyResponse {
        List<ProxyJson> data;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class ProxyJson {
        String ip;
        Integer port;
    }
}

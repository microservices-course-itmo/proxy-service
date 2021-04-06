package com.wine.to.up.proxy_service.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
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

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProxyClientImpl implements ProxyClient {

    @Value("${proxy.api-url}")
    private String apiUrl;

    @Value("${proxy.api-key}")
    private String apiKey;

    @Value("${proxy.api2-url}")
    private String api2Url;

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<Proxy> getProxyList() {
        //Building a request to the first proxy API
        HttpUrl.Builder builder = HttpUrl.parse(apiUrl).newBuilder();
        builder.addQueryParameter("key", apiKey);
        builder.addQueryParameter("type", "1");
        Request request = new Request.Builder().url(builder.build()).build();

        //Building a request to the second proxy API
        builder = HttpUrl.parse(api2Url).newBuilder();
        builder.addQueryParameter("type", "http");
        Request request2 = new Request.Builder().url(builder.build()).build();

        List<Proxy> proxyList = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            if (!(response.code() < 200 || response.code() > 204 || response.body() == null)) {
                ProxyResponse proxyResponse = mapper.readValue(response.body().bytes(), ProxyResponse.class);
                proxyList.addAll(proxyResponse.data.stream()
                        .map(proxy -> new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(proxy.ip, proxy.port)))
                        .collect(Collectors.toList()));
            }

            Response response2 = client.newCall(request2).execute();
            if (!(response2.code() < 200 || response2.code() > 204 || response2.body() == null)) {
                List<String> proxyServers = new ArrayList<>(Arrays.asList(response2.body().string().split("\n")));
                proxyList.addAll(proxyServers.stream().map(
                        proxy -> {
                            String[] ipAndPort = proxy.split(":");
                            int port = Integer.parseInt(ipAndPort[1].strip());
                            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipAndPort[0], port));
                        }
                ).collect(Collectors.toList()));
            }
            if (!proxyList.isEmpty()) {
                log.info("Proxy count : {}", proxyList.size());
                return proxyList;
            } else {
                return new ArrayList<>();
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
    static class ProxyResponseNew {
        List<String> data;
    }


    @NoArgsConstructor
    @Getter
    @Setter
    static class ProxyJson {
        String ip;
        Integer port;
    }
}

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

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<Proxy> getProxyList() {
        //Билд URL запроса с параметрами key и type
        HttpUrl.Builder builder = HttpUrl.parse(apiUrl).newBuilder();
        builder.addQueryParameter("key", apiKey);
        builder.addQueryParameter("type", "1");

        
        //Инициализация класса запроса
        Request request = new Request.Builder().url(builder.build()).build();

        builder = HttpUrl.parse("https://www.proxy-list.download/api/v1/get").newBuilder();
        builder.addQueryParameter("type", "http");


        Request r2 = new Request.Builder().url(builder.build()).build();

        try {
            //Получение ответа запрос
            Response response = client.newCall(request).execute();
            if (response.code() < 200 || response.code() > 204 || response.body() == null) {
                return Collections.emptyList();
            }

            ProxyResponse proxyResponse = mapper.readValue(response.body().bytes(), ProxyResponse.class);
            Response response2 = client.newCall(r2).execute();

            List<String> proxyServers =  new ArrayList<String>(Arrays.asList(response2.body().string().split("\n")));

            

            log.info("HEREEEEEEEEE {}", builder.build().toString());
            //наша склейка запросов
            // proxyResponse.data.addAll(proxyResponse2.data);

            List<Proxy> proxyList = proxyResponse.data.stream()
                    .map(proxy -> new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(proxy.ip, proxy.port)))
                    .collect(Collectors.toList());
            
            // proxyList.addAll(proxyServers.stream().
            //         map(str -> {
            //             new Proxy(Proxy.Type.HTTP,
            //             new InetSocketAddress(str.split(":")[0], Integer.parseInt(str.split(":")[1])))})
            //         .collect(Collectors.toList());

            for (String proxy : proxyServers) {
                String[] ipAndPort = proxy.split(":");
                proxyList.add(new Proxy(Proxy.Type.HTTP,  new InetSocketAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1].strip()))));
                
            }
            

            if(!proxyList.isEmpty()) {
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

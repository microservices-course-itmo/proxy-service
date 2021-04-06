package com.wine.to.up.proxy_service.job;

import com.wine.to.up.proxy_service.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ProxiesJob {

    private final ProxyService proxyService;

    public ProxiesJob(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Scheduled(cron = "${cron.job.get.proxies}")
    public void updateProxiesJob() {
        log.info("Started GetProxiesJob at {}", new Date());
        proxyService.updateProxies();
        proxyService.updateParserProxies();
        log.info("End GetProxiesJob at {}", new Date());
    }

    @Scheduled(cron = "${cron.job.get.proxies}")
    public void cleanProxies() {
        proxyService.deleteProxies();
    }
}

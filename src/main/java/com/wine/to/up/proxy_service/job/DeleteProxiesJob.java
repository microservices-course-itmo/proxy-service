package com.wine.to.up.proxy_service.job;

import com.wine.to.up.proxy_service.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DeleteProxiesJob {

    private final ProxyService proxyService;

    public DeleteProxiesJob(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Scheduled(cron = "${cron.job.delete.proxies}")
    public void runJob() {
        log.info("Started DeleteProxiesJob at {}", new Date());
        proxyService.deleteProxies();
        log.info("End DeleteProxiesJob at {}", new Date());
    }
}

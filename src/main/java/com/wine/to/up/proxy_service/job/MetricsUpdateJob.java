package com.wine.to.up.proxy_service.job;

import com.wine.to.up.proxy_service.entity.Parser;
import com.wine.to.up.proxy_service.metrics.ProxyMetricsCollector;
import com.wine.to.up.proxy_service.repository.ParserProxiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MetricsUpdateJob {

    private final ProxyMetricsCollector proxyMetricsCollector;
    private final ParserProxiesRepository parserProxiesRepository;

    public MetricsUpdateJob(
            ProxyMetricsCollector proxyMetricsCollector,
            ParserProxiesRepository parserProxiesRepository)
    {
        this.proxyMetricsCollector = proxyMetricsCollector;
        this.parserProxiesRepository = parserProxiesRepository;
    }

    @Scheduled(cron = "${cron.job.update.metrics}")
    public void updateMetrics() {
        for (Parser parser : Parser.values()) {
            int availableProxiesCount = parserProxiesRepository.getAllByParserOrderByPingAsc(parser).size();
            proxyMetricsCollector.setProxiesAvailable(parser, availableProxiesCount);
            log.debug("Available proxies for {}: {}", parser.name(), availableProxiesCount);
        }
        log.info("Updated metrics");
    }

}

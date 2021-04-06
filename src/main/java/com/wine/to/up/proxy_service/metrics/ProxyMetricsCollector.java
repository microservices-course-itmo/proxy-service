package com.wine.to.up.proxy_service.metrics;

import com.wine.to.up.proxy_service.entity.Parser;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.prometheus.client.Gauge;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProxyMetricsCollector {
    /* Labels and string literals */
    private static final String PROXIES_AVAILABLE = "proxies_available";
    private static final String PARSER_NAME_LABEL = "parser_name";

    /* Prometheus */
    private static final Gauge proxiesAvailablePrometheus = Gauge.build()
            .name(PROXIES_AVAILABLE)
            .labelNames(PARSER_NAME_LABEL)
            .help("Number of proxies available for the service")
            .register();

    /* Methods */
    public void setProxiesAvailable(Parser parser, int value) {
        proxiesAvailablePrometheus.labels(parser.name()).set(value);
        Metrics.gauge(PROXIES_AVAILABLE, List.of(Tag.of(PARSER_NAME_LABEL, parser.name())), value); // грязный костыль да я хз есть ли более красивый способ передать Iterable<Tag>
    }
}

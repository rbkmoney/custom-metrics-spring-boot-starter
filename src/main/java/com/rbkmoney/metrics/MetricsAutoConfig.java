package com.rbkmoney.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config()
                .commonTags("application", applicationName);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomMetricBinder customMetricBinder() {
        return new CustomMetricBinder();
    }
}

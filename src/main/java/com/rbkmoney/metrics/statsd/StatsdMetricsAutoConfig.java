package com.rbkmoney.metrics.statsd;

        import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

@Configuration
public class StatsdMetricsAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public CustomMetricBinder customMetricBinder() {
        return new CustomMetricBinder();
    }
}

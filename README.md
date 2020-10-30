# Starter for StatsD and Prometheus custom metrics

Для включения экспорта метрик указать параметры в `application.yml`.

## StatsD

``` yaml
management:
    metrics:
        export:
            statsd:
                enabled: true
                flavor: etsy
                # specify StatsD address
                host: changeit 
                port: 8125
```

## Prometheus

``` yaml
management:
    server:
        # optional – use 8022 for backward compatibility with wetkitty
        port: 8023 
    endpoint: 
        metrics:
            enavled: true
        prometheus:
            enabled: true
    endpoints:
        web:
            exposure:
                include: health,info,prometheus
    metrics:
        export:
            prometheus:
                enabled: true
```

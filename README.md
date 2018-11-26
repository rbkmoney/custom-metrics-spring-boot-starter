# starter for statsd metrics

Нужно указать параметры в application.properties

```
management.metrics.export.statsd.enabled=true
management.metrics.export.statsd.flavor=etsy
management.metrics.export.statsd.host=STATSD_ADDRESS
management.metrics.export.statsd.port=8125
```
package com.rbkmoney.metrics.statsd;

import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.UnixOperatingSystemMXBean;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

public class CustomMetricBinder implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        registerJvmMetrics(meterRegistry);
        registerMemoryPoolMetrics(meterRegistry);
        registerClassesMetrics(meterRegistry);
        registerFileDescriptionMetrics(meterRegistry);
        registerGcMetrics(meterRegistry);
    }

    private void registerGcMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(o -> (GarbageCollectorMXBean) o).forEach(garbageCollectorMXBean -> {
            Gauge.builder("jvm.gc.count", garbageCollectorMXBean, GarbageCollectorMXBean::getCollectionCount)
                    .baseUnit("collections").register(meterRegistry);
            Gauge.builder("jvm.gc.time", garbageCollectorMXBean, GarbageCollectorMXBean::getCollectionTime)
                    .baseUnit("millis").register(meterRegistry);
        });
    }

    private void registerFileDescriptionMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(UnixOperatingSystemMXBean.class).forEach(classLoadingMXBean -> {
            Gauge.builder("jvm.max.file.descriptor.count", classLoadingMXBean, UnixOperatingSystemMXBean::getMaxFileDescriptorCount)
                    .baseUnit("descriptors").register(meterRegistry);
            Gauge.builder("jvm.open.file.descriptor.count", classLoadingMXBean, UnixOperatingSystemMXBean::getOpenFileDescriptorCount)
                    .baseUnit("descriptors").register(meterRegistry);
        });
    }

    private void registerClassesMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(ClassLoadingMXBean.class).forEach(classLoadingMXBean -> {
            Gauge.builder("jvm.classes.total.loaded", classLoadingMXBean, ClassLoadingMXBean::getTotalLoadedClassCount)
                    .baseUnit("classes").register(meterRegistry);
            Gauge.builder("jvm.classes.loaded", classLoadingMXBean, ClassLoadingMXBean::getLoadedClassCount)
                    .baseUnit("classes").register(meterRegistry);
        });
    }

    private void registerMemoryPoolMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class).forEach(memoryPoolBean -> {
            Gauge.builder("jvm.memory.used", memoryPoolBean,
                    (mem) -> (double) mem.getUsage().getUsed()).baseUnit("bytes").register(meterRegistry);
            Gauge.builder("jvm.memory.committed", memoryPoolBean,
                    (mem) -> (double) mem.getUsage().getCommitted()).baseUnit("bytes").register(meterRegistry);
            Gauge.builder("jvm.memory.max", memoryPoolBean,
                    (mem) -> (double) mem.getUsage().getMax()).baseUnit("bytes").register(meterRegistry);
        });
    }

    private void registerJvmMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(OperatingSystemMXBean.class).forEach(memoryPoolBean -> {
            Gauge.builder("free.physical.memory.size", memoryPoolBean, OperatingSystemMXBean::getFreePhysicalMemorySize)
                    .baseUnit("bytes").register(meterRegistry);
            Gauge.builder("total.physical.memory.size", memoryPoolBean, OperatingSystemMXBean::getTotalPhysicalMemorySize)
                    .baseUnit("bytes").register(meterRegistry);
            Gauge.builder("system.cpu.load", memoryPoolBean, OperatingSystemMXBean::getSystemCpuLoad)
                    .baseUnit("bytes").register(meterRegistry);
            Gauge.builder("process.cpu.load", memoryPoolBean, OperatingSystemMXBean::getProcessCpuLoad)
                    .baseUnit("bytes").register(meterRegistry);
            Gauge.builder("total.swap", memoryPoolBean, OperatingSystemMXBean::getTotalSwapSpaceSize)
                    .baseUnit("bytes").register(meterRegistry);
            Gauge.builder("free.swap", memoryPoolBean, OperatingSystemMXBean::getFreeSwapSpaceSize)
                    .baseUnit("bytes").register(meterRegistry);
        });
    }
}

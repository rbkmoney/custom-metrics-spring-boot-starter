package com.rbkmoney.metrics;

import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.UnixOperatingSystemMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

import static io.micrometer.core.instrument.Gauge.builder;

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
                .map(o -> (GarbageCollectorMXBean) o)
                .forEach(garbageCollectorMXBean -> {
                    builder("jvm.gc.count", garbageCollectorMXBean, GarbageCollectorMXBean::getCollectionCount)
                            .baseUnit("collections").register(meterRegistry);
                    builder("jvm.gc.time", garbageCollectorMXBean, GarbageCollectorMXBean::getCollectionTime)
                            .baseUnit("millis").register(meterRegistry);
                });
    }

    private void registerFileDescriptionMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(UnixOperatingSystemMXBean.class).forEach(classLoadingMXBean -> {
            builder(
                    "jvm.max.file.descriptor.count",
                    classLoadingMXBean,
                    UnixOperatingSystemMXBean::getMaxFileDescriptorCount)
                    .baseUnit("descriptors").register(meterRegistry);
            builder(
                    "jvm.open.file.descriptor.count",
                    classLoadingMXBean,
                    UnixOperatingSystemMXBean::getOpenFileDescriptorCount)
                    .baseUnit("descriptors").register(meterRegistry);
        });
    }

    private void registerClassesMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(ClassLoadingMXBean.class).forEach(classLoadingMXBean -> {
            builder("jvm.classes.total.loaded", classLoadingMXBean, ClassLoadingMXBean::getTotalLoadedClassCount)
                    .baseUnit("classes").register(meterRegistry);
            builder("jvm.classes.loaded", classLoadingMXBean, ClassLoadingMXBean::getLoadedClassCount)
                    .baseUnit("classes").register(meterRegistry);
        });
    }

    private void registerMemoryPoolMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class).forEach(memoryPoolBean -> {
            String area = memoryPoolBean.getType().equals(MemoryType.HEAP) ? "heap" : "nonheap";
            Iterable<Tag> tags = Tags.of("id", memoryPoolBean.getName(), "area", area);

            builder("jvm.memory.used", memoryPoolBean, (mem) -> (double) mem.getUsage().getUsed())
                    .baseUnit("bytes").tags(tags).register(meterRegistry);
            builder("jvm.memory.committed", memoryPoolBean, (mem) -> (double) mem.getUsage().getCommitted())
                    .baseUnit("bytes").tags(tags).register(meterRegistry);
            builder("jvm.memory.max", memoryPoolBean, (mem) -> (double) mem.getUsage().getMax())
                    .baseUnit("bytes").tags(tags).register(meterRegistry);
        });
    }

    private void registerJvmMetrics(MeterRegistry meterRegistry) {
        ManagementFactory.getPlatformMXBeans(OperatingSystemMXBean.class).forEach(memoryPoolBean -> {
            builder(
                    "free.physical.memory.size",
                    memoryPoolBean,
                    OperatingSystemMXBean::getFreePhysicalMemorySize)
                    .baseUnit("bytes").register(meterRegistry);
            builder(
                    "total.physical.memory.size",
                    memoryPoolBean,
                    OperatingSystemMXBean::getTotalPhysicalMemorySize)
                    .baseUnit("bytes").register(meterRegistry);
            builder("system.cpu.load", memoryPoolBean, OperatingSystemMXBean::getSystemCpuLoad)
                    .baseUnit("bytes").register(meterRegistry);
            builder("process.cpu.load", memoryPoolBean, OperatingSystemMXBean::getProcessCpuLoad)
                    .baseUnit("bytes").register(meterRegistry);
            builder("total.swap", memoryPoolBean, OperatingSystemMXBean::getTotalSwapSpaceSize)
                    .baseUnit("bytes").register(meterRegistry);
            builder("free.swap", memoryPoolBean, OperatingSystemMXBean::getFreeSwapSpaceSize)
                    .baseUnit("bytes").register(meterRegistry);
        });
    }
}

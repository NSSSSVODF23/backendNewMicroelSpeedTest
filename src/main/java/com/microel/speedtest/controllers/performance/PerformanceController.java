package com.microel.speedtest.controllers.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.util.concurrent.AtomicDouble;
import com.microel.speedtest.common.models.chart.TimeDoublePoint;
import com.microel.speedtest.common.models.chart.TimeLongPoint;
import org.springframework.scheduling.annotation.Scheduled;

import com.microel.speedtest.repositories.CpuUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.NetworkUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.RamUtilRepositoryDispatcher;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;
import reactor.core.publisher.Sinks;

@Service
public class PerformanceController {

    /**
     * Указывает сколько информации будет храниться, и через сколько тиков сохранять статистику
     */
    private final Integer STATISTIC_TIMER_LIMIT = 300;
    private final SystemInfo system = new SystemInfo();
    private final List<NetworkIF> nifs = Collections.synchronizedList(new ArrayList<>());

    private final AtomicLong receivedBytes = new AtomicLong(0L);
    private final AtomicLong transceivedBytes = new AtomicLong(0L);

    private final AtomicDouble receivedSpeed = new AtomicDouble(0d);
    private final AtomicDouble transceivedSpeed = new AtomicDouble(0d);

    private final PerformanceInfo info = new PerformanceInfo(STATISTIC_TIMER_LIMIT);

    private final Sinks.Many<PerformanceInfo> updatePerformanceSink;

    private final NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher;

    private final CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher;

    private final RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher;

    private final AtomicInteger statisticSaveTimer = new AtomicInteger(0);

    public PerformanceController(Sinks.Many<PerformanceInfo> updatePerformanceSink, NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher, CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher, RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher) {
        getNetworkIfs();
        this.updatePerformanceSink = updatePerformanceSink;
        this.networkUtilRepositoryDispatcher = networkUtilRepositoryDispatcher;
        this.cpuUtilRepositoryDispatcher = cpuUtilRepositoryDispatcher;
        this.ramUtilRepositoryDispatcher = ramUtilRepositoryDispatcher;
    }

    @Scheduled(fixedRate = 1000)
    private void start() {
        updateNifsInfo();

        Long currentReceivedBytes = getRxBytes();
        Long currentTransceivedBytes = getTxBytes();

        if (receivedBytes.get() == 0L) {
            receivedBytes.set(currentReceivedBytes);
        }
        if (transceivedBytes.get() == 0L) {
            transceivedBytes.set(currentTransceivedBytes);
        }

        receivedSpeed.set(((currentReceivedBytes - receivedBytes.get()) * 8) / 1000000d);
        transceivedSpeed.set(((currentTransceivedBytes - transceivedBytes.get()) * 8) / 1000000d);

        if(receivedSpeed.get()<0) receivedSpeed.set(0d);
        if(transceivedSpeed.get()<0) transceivedSpeed.set(0d);

        receivedBytes.set(currentReceivedBytes);
        transceivedBytes.set(currentTransceivedBytes);

        info.getReceivedChartData().add(new TimeDoublePoint(receivedSpeed.get()));
        info.getTransceivedChartData().add(new TimeDoublePoint(transceivedSpeed.get()));
        info.getCpuChartData().add(new TimeDoublePoint(getCpuUtilization()));
        info.getMemoryChartData().add(new TimeLongPoint(getUtilizedRamCount()));
        info.getTotalMemoryChartData().add(new TimeLongPoint(getTotalRamCount()));

        updatePerformanceSink.tryEmitNext(info);

        if (statisticSaveTimer.incrementAndGet() == STATISTIC_TIMER_LIMIT) {
            statisticSaveTimer.set(0);
            networkUtilRepositoryDispatcher.save(info.getNetworkStatisticSnapshot());
            cpuUtilRepositoryDispatcher.save(info.getCpuStatisticSnapshot());
            ramUtilRepositoryDispatcher.save(info.getMemoryStatisticSnapshot());
        }
    }

    private Long getRxBytes() {
        if (isNifsEmpty())
            return 0L;
        return nifs.stream().map(NetworkIF::getBytesRecv).reduce(0L, Long::sum);
    }

    private Long getTxBytes() {
        if (isNifsEmpty())
            return 0L;
        return nifs.stream().map(NetworkIF::getBytesSent).reduce(0L, Long::sum);
    }

    private Double getCpuUtilization() {
        return system.getHardware().getProcessor().getSystemCpuLoad(1000) * 100;
    }

    private Long getTotalRamCount() {
        return system.getHardware().getMemory().getTotal() / 1000000;
    }

    private Long getUtilizedRamCount() {
        return (system.getHardware().getMemory().getTotal() - system.getHardware().getMemory().getAvailable())
                / 1000000;
    }

    private void updateNifsInfo() {
        nifs.forEach(NetworkIF::updateAttributes);
    }

    private void getNetworkIfs() {
        nifs.clear();
        nifs.addAll(system.getHardware().getNetworkIFs());
    }

    private Boolean isNifsEmpty() {
        return nifs.size() == 0;
    }

}

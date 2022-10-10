package com.microel.speedtest.controllers.performance;

import java.util.List;

import com.microel.speedtest.common.models.chart.TimeDoublePoint;
import com.microel.speedtest.common.models.chart.TimeLongPoint;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microel.speedtest.common.models.chart.ChartPoint;
import com.microel.speedtest.repositories.CpuUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.NetworkUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.RamUtilRepositoryDispatcher;

import oshi.SystemInfo;
import oshi.hardware.NetworkIF;
import reactor.core.publisher.Sinks;

@Lazy(false)
@Component
public class PerformanceController {

    /**
     * Указывает сколько информации будет храниться, и через сколько тиков сохранять статистику
     */
    private final Integer STATISTIC_TIMER_LIMIT = 300;
    private SystemInfo system = new SystemInfo();
    private final List<NetworkIF> nifs;


    private Long receivedBytes = 0L;
    private Long transceivedBytes = 0L;

    private Double receivedSpeed = 0d;
    private Double transceivedSpeed = 0d;

    private PerformanceInfo info = new PerformanceInfo(STATISTIC_TIMER_LIMIT);

    private final Sinks.Many<PerformanceInfo> updatePerformanceSink;

    private final NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher;

    private final CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher;

    private final RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher;

    private Integer statisticSaveTimer = 0;

    public PerformanceController(Sinks.Many<PerformanceInfo> updatePerformanceSink, NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher, CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher, RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher) {
        nifs = getNetworkIfs();
        this.updatePerformanceSink = updatePerformanceSink;
        this.networkUtilRepositoryDispatcher = networkUtilRepositoryDispatcher;
        this.cpuUtilRepositoryDispatcher = cpuUtilRepositoryDispatcher;
        this.ramUtilRepositoryDispatcher = ramUtilRepositoryDispatcher;
    }

    @Scheduled(fixedRate = 1000)
    private void start() {
        if (isNifsEmpty())
            return;

        updateNifsInfo();

        Long currentReceivedBytes = getRxBytes();
        Long currentTransceivedBytes = getTxBytes();

        if (receivedBytes == 0L) {
            receivedBytes = currentReceivedBytes;
        }
        if (transceivedBytes == 0L) {
            transceivedBytes = currentTransceivedBytes;
        }

        receivedSpeed = ((currentReceivedBytes - receivedBytes) * 8) / 1000000d;
        transceivedSpeed = ((currentTransceivedBytes - transceivedBytes) * 8) / 1000000d;

        if(receivedSpeed<0) receivedSpeed = 0d;
        if(transceivedSpeed<0) transceivedSpeed = 0d;

        receivedBytes = currentReceivedBytes;
        transceivedBytes = currentTransceivedBytes;

        info.getReceivedChartData().add(new TimeDoublePoint(receivedSpeed));
        info.getTransceivedChartData().add(new TimeDoublePoint(transceivedSpeed));
        info.getCpuChartData().add(new TimeDoublePoint(getCpuUtilization()));
        info.getMemoryChartData().add(new TimeLongPoint(getUtilizedRamCount()));
        info.getTotalMemoryChartData().add(new TimeLongPoint(getTotalRamCount()));

        updatePerformanceSink.tryEmitNext(info.copy());

        if ((++statisticSaveTimer).equals(STATISTIC_TIMER_LIMIT)) {
            statisticSaveTimer = 0;
            networkUtilRepositoryDispatcher.save(info.getNetworkStatisticSnapshot());
            cpuUtilRepositoryDispatcher.save(info.getCpuStatisticSnapshot());
            ramUtilRepositoryDispatcher.save(info.getMemoryStatisticSnapshot());
        }
    }

    private Long getRxBytes() {
        if (isNifsEmpty())
            return 0L;
        return nifs.stream().map(nif -> nif.getBytesRecv()).reduce(0L, Long::sum);
    }

    private Long getTxBytes() {
        if (isNifsEmpty())
            return 0L;
        return nifs.stream().map(nif -> nif.getBytesSent()).reduce(0L, Long::sum);
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
        nifs.stream().forEach(nif -> nif.updateAttributes());
    }

    private List<NetworkIF> getNetworkIfs() {
        return system.getHardware().getNetworkIFs();
    }

    private Boolean isNifsEmpty() {
        return nifs == null || nifs.size() == 0;
    }

}

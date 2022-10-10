package com.microel.speedtest.controllers.performance;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.microel.speedtest.common.models.chart.ChartPoint;
import com.microel.speedtest.common.models.FixedList;
import com.microel.speedtest.common.models.chart.TimeDoublePoint;
import com.microel.speedtest.common.models.chart.TimeLongPoint;
import com.microel.speedtest.repositories.entities.CpuUtil;
import com.microel.speedtest.repositories.entities.NetworkUtil;
import com.microel.speedtest.repositories.entities.RamUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceInfo {

    private List<TimeDoublePoint> receivedChartData;
    private List<TimeDoublePoint> transceivedChartData;
    private List<TimeDoublePoint> cpuChartData;
    private List<TimeLongPoint> memoryChartData;
    private List<TimeLongPoint> totalMemoryChartData;
    private final Integer limit;

    public PerformanceInfo(Integer limit){
        this.limit = limit;
        receivedChartData = Collections.synchronizedList(new FixedList<>(limit));
        transceivedChartData = Collections.synchronizedList( new FixedList<>(limit));
        cpuChartData = Collections.synchronizedList( new FixedList<>(limit));
        memoryChartData = Collections.synchronizedList( new FixedList<>(limit));
        totalMemoryChartData = Collections.synchronizedList( new FixedList<>(limit));
    }

    public PerformanceInfo copy() {
        PerformanceInfo copyObj = new PerformanceInfo(limit);
        copyObj.setReceivedChartData(new ArrayList<>(receivedChartData));
        copyObj.setTransceivedChartData(new ArrayList<>(transceivedChartData));
        copyObj.setCpuChartData(new ArrayList<>(cpuChartData));
        copyObj.setMemoryChartData(new ArrayList<>(memoryChartData));
        copyObj.setTotalMemoryChartData(new ArrayList<>(totalMemoryChartData));
        return copyObj;
    }

    public NetworkUtil getNetworkStatisticSnapshot() {
        final Double rx = receivedChartData.stream().map(ChartPoint::getY).reduce(0d, Double::sum)
                / receivedChartData.size();
        final Double tx = transceivedChartData.stream().map(ChartPoint::getY).reduce(0d, Double::sum)
                / transceivedChartData.size();
        return NetworkUtil.builder().rx(rx).tx(tx).stamp(Timestamp.from(Instant.now())).build();
    }

    public CpuUtil getCpuStatisticSnapshot() {
        final Double load = cpuChartData.stream().map(ChartPoint::getY).reduce(0d, Double::sum) / cpuChartData.size();
        return CpuUtil.builder().load(load).stamp(Timestamp.from(Instant.now())).build();
    }

    public RamUtil getMemoryStatisticSnapshot() {
        final Long util = memoryChartData.stream().map(ChartPoint::getY).reduce(0l, Long::sum) / memoryChartData.size();
        final Long total = totalMemoryChartData.stream().map(ChartPoint::getY).reduce(0l, Long::sum)
                / totalMemoryChartData.size();
        return RamUtil.builder().utilized(util).total(total).stamp(Timestamp.from(Instant.now())).build();
    }
}

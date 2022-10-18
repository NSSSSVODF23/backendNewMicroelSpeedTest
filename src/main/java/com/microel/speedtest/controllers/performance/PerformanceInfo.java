package com.microel.speedtest.controllers.performance;

import com.microel.speedtest.common.models.FixedList;
import com.microel.speedtest.common.models.chart.ChartPoint;
import com.microel.speedtest.common.models.chart.TimeDoublePoint;
import com.microel.speedtest.common.models.chart.TimeLongPoint;
import com.microel.speedtest.repositories.entities.CpuUtil;
import com.microel.speedtest.repositories.entities.NetworkUtil;
import com.microel.speedtest.repositories.entities.RamUtil;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class PerformanceInfo {

    private List<TimeDoublePoint> receivedChartData;
    private List<TimeDoublePoint> transceivedChartData;
    private List<TimeDoublePoint> cpuChartData;
    private List<TimeLongPoint> memoryChartData;
    private List<TimeLongPoint> totalMemoryChartData;
    private final Integer limit;

    public PerformanceInfo(Integer limit) {
        this.limit = limit;
        receivedChartData = Collections.synchronizedList(new FixedList<>(limit));
        transceivedChartData = Collections.synchronizedList(new FixedList<>(limit));
        cpuChartData = Collections.synchronizedList(new FixedList<>(limit));
        memoryChartData = Collections.synchronizedList(new FixedList<>(limit));
        totalMemoryChartData = Collections.synchronizedList(new FixedList<>(limit));
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
        return NetworkUtil.builder().rx(topAvg(receivedChartData)).tx(topAvg(transceivedChartData)).stamp(Timestamp.from(Instant.now())).build();
    }

    private double topAvg(List<TimeDoublePoint> chartData) {
        double result = 0d;
        if (chartData.size() > 0) {
            List<Double> prep = chartData.stream().map(ChartPoint::getY).sorted((o1, o2) -> (int) (o2 - o1)).limit(15).collect(Collectors.toList());
            result = prep.stream().reduce(0d, Double::sum) / (double) prep.size();
        }
        return result;
    }

    public CpuUtil getCpuStatisticSnapshot() {
        return CpuUtil.builder().load(topAvg(cpuChartData)).stamp(Timestamp.from(Instant.now())).build();
    }

    public RamUtil getMemoryStatisticSnapshot() {

        long util = 0L;
        if (memoryChartData.size() > 0)
            util = memoryChartData.stream().map(ChartPoint::getY).reduce(0L, Long::sum) / memoryChartData.size();

        long total = 0L;
        if (totalMemoryChartData.size() > 0)
            total = totalMemoryChartData.stream().map(ChartPoint::getY).reduce(0L, Long::sum) / totalMemoryChartData.size();

        return RamUtil.builder().utilized(util).total(total).stamp(Timestamp.from(Instant.now())).build();
    }
}

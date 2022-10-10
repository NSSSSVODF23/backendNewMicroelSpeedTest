package com.microel.speedtest.services.resolvers.query;

import com.microel.speedtest.common.models.chart.GroupDayCTypeIntegerPoint;
import com.microel.speedtest.common.models.chart.GroupStringCTypeIntegerPoint;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.repositories.CpuUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.MeasureRepositoryDispatcher;
import com.microel.speedtest.repositories.NetworkUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.RamUtilRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.CpuUtil;
import com.microel.speedtest.repositories.entities.NetworkUtil;
import com.microel.speedtest.repositories.entities.RamUtil;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticQueries implements GraphQLQueryResolver {

    private final NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher;
    private final CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher;
    private final RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher;
    private MeasureRepositoryDispatcher measureRepositoryDispatcher;

    public StatisticQueries(NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher,
                            CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher,
                            RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher, MeasureRepositoryDispatcher measureRepositoryDispatcher) {
        this.networkUtilRepositoryDispatcher = networkUtilRepositoryDispatcher;
        this.cpuUtilRepositoryDispatcher = cpuUtilRepositoryDispatcher;
        this.ramUtilRepositoryDispatcher = ramUtilRepositoryDispatcher;
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
    }

    public List<NetworkUtil> getNetworkStatistic(TimeRange timeRange){
        return networkUtilRepositoryDispatcher.getStatistic(timeRange);
    }

    public List<CpuUtil> getCpuStatistic(TimeRange timeRange){
        return cpuUtilRepositoryDispatcher.getStatistic(timeRange);
    }

    public List<RamUtil> getRamStatistic(TimeRange timeRange){
        return  ramUtilRepositoryDispatcher.getStatistic(timeRange);
    }

    public List<GroupDayCTypeIntegerPoint> getMeasuringCountsInDays(TimeRange timeRange) {
        return measureRepositoryDispatcher.getCountsInDays(timeRange);
    }

    public List<GroupStringCTypeIntegerPoint> getMeasuringCountsInAddresses(TimeRange timeRange){
        return measureRepositoryDispatcher.getCountsInAddresses(timeRange);
    }

    public Integer getMeasuringCountsFromPeriod(TimeRange timeRange){
        return measureRepositoryDispatcher.countByStampBetween(timeRange);
    }
}


package com.microel.speedtest.services.resolvers.query;

import com.microel.speedtest.common.models.chart.*;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.repositories.*;
import com.microel.speedtest.repositories.entities.CpuUtil;
import com.microel.speedtest.repositories.entities.NetworkUtil;
import com.microel.speedtest.repositories.entities.RamUtil;
import com.microel.speedtest.repositories.proxies.StringDoublePointProxy;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticQueries implements GraphQLQueryResolver {

    private final NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher;
    private final CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher;
    private final RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher;

    private final ComplaintRepositoryDispatcher complaintRepositoryDispatcher;
    private final MeasureRepositoryDispatcher measureRepositoryDispatcher;

    private final FeedbackRepositoryDispatcher feedbackRepositoryDispatcher;

    public StatisticQueries(NetworkUtilRepositoryDispatcher networkUtilRepositoryDispatcher,
                            CpuUtilRepositoryDispatcher cpuUtilRepositoryDispatcher,
                            RamUtilRepositoryDispatcher ramUtilRepositoryDispatcher, ComplaintRepositoryDispatcher complaintRepositoryDispatcher, MeasureRepositoryDispatcher measureRepositoryDispatcher, FeedbackRepositoryDispatcher feedbackRepositoryDispatcher) {
        this.networkUtilRepositoryDispatcher = networkUtilRepositoryDispatcher;
        this.cpuUtilRepositoryDispatcher = cpuUtilRepositoryDispatcher;
        this.ramUtilRepositoryDispatcher = ramUtilRepositoryDispatcher;
        this.complaintRepositoryDispatcher = complaintRepositoryDispatcher;
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
        this.feedbackRepositoryDispatcher = feedbackRepositoryDispatcher;
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

    public List<GroupDateCTypeIntegerPoint> getMeasuringCountsInDate(TimeRange timeRange) {
        return measureRepositoryDispatcher.getCountsInDate(timeRange);
    }

    public List<GroupDayCTypeIntegerPoint> getMeasuringCountsInDays(TimeRange timeRange) {
        return measureRepositoryDispatcher.getCountsInDay(timeRange);
    }

    public List<GroupHourCTypeIntegerPoint> getMeasuringCountsInHour(TimeRange timeRange) {
        return measureRepositoryDispatcher.getCountsInHour(timeRange);
    }

    public List<GroupStringCTypeIntegerPoint> getMeasuringCountsInAddresses(TimeRange timeRange){
        return measureRepositoryDispatcher.getCountsInAddresses(timeRange);
    }

    public Integer getMeasuringCountsFromPeriod(TimeRange timeRange){
        return measureRepositoryDispatcher.countByStampBetween(timeRange);
    }

    public List<DateIntegerPoint> getComplaintCountsInDate(TimeRange timeRange) {
        return complaintRepositoryDispatcher.getCountsInDate(timeRange);
    }

    public List<DOWIntegerPoint> getComplaintCountsInDays(TimeRange timeRange) {
        return complaintRepositoryDispatcher.getCountsInDay(timeRange);
    }

    public List<HourIntegerPoint> getComplaintCountsInHour(TimeRange timeRange) {
        return complaintRepositoryDispatcher.getCountsInHour(timeRange);
    }

    public List<StringIntegerPoint> getComplaintCountsInAddresses(TimeRange timeRange){
        return complaintRepositoryDispatcher.getCountsInAddresses(timeRange);
    }

    public List<StringDoublePointProxy> getFeedbackAvgInAddresses(TimeRange timeRange){
        return feedbackRepositoryDispatcher.getAvgInAddresses(timeRange);
    }
}


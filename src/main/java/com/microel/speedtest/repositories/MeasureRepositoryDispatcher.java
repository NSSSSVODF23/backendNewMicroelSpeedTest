package com.microel.speedtest.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.models.QueryLimit;
import com.microel.speedtest.common.models.chart.GroupDayCTypeIntegerPoint;
import com.microel.speedtest.common.models.chart.GroupHourCTypeIntegerPoint;
import com.microel.speedtest.common.models.filters.MatchingFactory;
import com.microel.speedtest.common.models.updateprovides.MeasureUpdateProvider;
import com.microel.speedtest.common.models.chart.GroupDateCTypeIntegerPoint;
import com.microel.speedtest.common.models.chart.GroupStringCTypeIntegerPoint;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.repositories.entities.AcpSession;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.stereotype.Component;

import com.microel.speedtest.common.models.filters.MeasureFilter;
import com.microel.speedtest.repositories.entities.Measure;
import com.microel.speedtest.repositories.interfaces.MeasureRepository;

import reactor.core.publisher.Sinks;

import javax.persistence.criteria.Predicate;

@Component
public class MeasureRepositoryDispatcher {
    private final MeasureRepository measureRepository;

    private final SessionRepositoryDispatcher sessionRepositoryDispatcher;

    private final HouseRepositoryDispatcher houseRepositoryDispatcher;

    private final Sinks.Many<MeasureUpdateProvider> newMeasureSink;

    public MeasureRepositoryDispatcher(MeasureRepository measureRepository, SessionRepositoryDispatcher sessionRepositoryDispatcher,
                                       HouseRepositoryDispatcher houseRepositoryDispatcher, Sinks.Many<MeasureUpdateProvider> newMeasureSink) {
        this.measureRepository = measureRepository;
        this.sessionRepositoryDispatcher = sessionRepositoryDispatcher;
        this.houseRepositoryDispatcher = houseRepositoryDispatcher;
        this.newMeasureSink = newMeasureSink;
    }

    public Measure save(Measure measure) {
        // Устанавливаем существующий id если дом уже существует в БД
        measure.getSession().getHouse()
                .setHouseId(houseRepositoryDispatcher.getHouseId(measure.getSession().getHouse()));
        // Устанавливаем существующий id если сессия уже существует в БД
        measure.getSession().setSessionId(sessionRepositoryDispatcher.getSessionId(measure.getSession()));
        final Measure savedMeasure = measureRepository.save(measure);
        newMeasureSink.tryEmitNext(new MeasureUpdateProvider(ListMutationTypes.ADD, savedMeasure));
        return savedMeasure;
    }

    public Measure findById(Long id) {
        return measureRepository.findById(id).orElse(null);
    }

    public Page<Measure> find(Example<Measure> example, TimeRange timeRange, Pageable pageable) {
        return measureRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!timeRange.isEmpty()) predicates.add(criteriaBuilder.between(root.get("created"),timeRange.getStart(),timeRange.getEnd()));
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, criteriaBuilder, example));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        },pageable);
    }

    public List<Measure> findAll() {
        return measureRepository.findAll();
    }

    public Integer getAllMeasuresCount(MeasureFilter filter) {
        return measureRepository.getNumberOfFilteredMeasures(filter.getLogin(), filter.getAddress(), filter.getIp(),
                filter.getMac(), filter.getStart(), filter.getEnd());
    }

    public List<GroupDateCTypeIntegerPoint> getCountsInDate(TimeRange timeRange) {
        return measureRepository.getCountsInDate(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(GroupDateCTypeIntegerPoint::fromProxy).collect(Collectors.toList());
    }

    public List<GroupDayCTypeIntegerPoint> getCountsInDay(TimeRange timeRange) {
        return measureRepository.getCountsInDay(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(GroupDayCTypeIntegerPoint::fromProxy).collect(Collectors.toList());
    }

    public List<GroupHourCTypeIntegerPoint> getCountsInHour(TimeRange timeRange) {
        return measureRepository.getCountsInHour(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(GroupHourCTypeIntegerPoint::fromProxy).collect(Collectors.toList());
    }

    public List<GroupStringCTypeIntegerPoint> getCountsInAddresses(TimeRange timeRange){
        return measureRepository.getCountsInAddresses(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(GroupStringCTypeIntegerPoint::fromProxy).collect(Collectors.toList());
    }

    public Integer countByStampBetween(TimeRange timeRange){
        return  measureRepository.countByCreatedBetween(timeRange.getStart(), timeRange.getEnd());
    }

    public Page<Measure> findByLoginLastTen(String login) {
        Example<Measure> example = MatchingFactory.standardExample(Measure.builder().session(AcpSession.builder().login(login).build()).build());
        Pageable pageable = MatchingFactory.paginator(QueryLimit.of(0,10), Sort.by(Sort.Direction.DESC, "created"));
        return measureRepository.findAll(example,pageable);
    }
}

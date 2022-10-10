package com.microel.speedtest.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.models.updateprovides.MeasureUpdateProvider;
import com.microel.speedtest.common.models.chart.GroupDayCTypeIntegerPoint;
import com.microel.speedtest.common.models.chart.GroupStringCTypeIntegerPoint;
import com.microel.speedtest.common.models.TimeRange;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public List<GroupDayCTypeIntegerPoint> getCountsInDays(TimeRange timeRange) {
        return measureRepository.getCountsInDays(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(proxy-> GroupDayCTypeIntegerPoint.fromProxy(proxy)).collect(Collectors.toList());
    }

    public List<GroupStringCTypeIntegerPoint> getCountsInAddresses(TimeRange timeRange){
        return measureRepository.getCountsInAddresses(timeRange.getStart().toString(),timeRange.getEnd().toString()).stream().map(proxy->GroupStringCTypeIntegerPoint.fromProxy(proxy)).collect(Collectors.toList());
    }

    public Integer countByStampBetween(TimeRange timeRange){
        return  measureRepository.countByCreatedBetween(timeRange.getStart(), timeRange.getEnd());
    }
}

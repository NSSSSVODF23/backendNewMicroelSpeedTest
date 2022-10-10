package com.microel.speedtest.services.resolvers.query;

import java.util.List;

import com.microel.speedtest.common.models.ActiveSession;
import com.microel.speedtest.common.models.BeginningMeasure;
import com.microel.speedtest.common.models.QueryLimit;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.common.models.filters.MatchingFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microel.speedtest.common.models.filters.MeasureFilter;
import com.microel.speedtest.controllers.measure.MeasureController;
import com.microel.speedtest.repositories.MeasureRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Measure;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeasurementQueries implements GraphQLQueryResolver {

    private final MeasureRepositoryDispatcher measureRepositoryDispatcher;

    private final MeasureController measureController;

    public MeasurementQueries(MeasureRepositoryDispatcher measureRepositoryDispatcher, MeasureController measureController) {
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
        this.measureController = measureController;
    }

    public Measure getMeasure(Long id) {
        return measureRepositoryDispatcher.findById(id);
    }

    public List<Measure> getAllMeasures() {
        return measureRepositoryDispatcher.findAll();
    }

    public Page<Measure> getMeasures(Measure matchingObject, QueryLimit limits, TimeRange dateFilter) {
        return measureRepositoryDispatcher.find(MatchingFactory.standardExample(matchingObject), dateFilter,MatchingFactory.paginator(limits, Sort.by(Sort.Direction.DESC,"created")) );
    }

    public List<BeginningMeasure> getBeginningMeasures() {
        return measureController.getBeginningMeasures();
    }

    public List<ActiveSession> getActiveSession() { return measureController.getActiveSessions(); }

    public Integer getTotalMeasures(MeasureFilter filter) {
        final Integer count = measureRepositoryDispatcher.getAllMeasuresCount(filter);
        log.info("Total: {}", count);
        return count;
    }
}

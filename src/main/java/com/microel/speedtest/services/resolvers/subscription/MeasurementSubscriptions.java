package com.microel.speedtest.services.resolvers.subscription;

import com.microel.speedtest.common.models.updateprovides.ActiveSessionsUpdateProvider;
import com.microel.speedtest.common.models.updateprovides.BeginningMeasureUpdateProvider;
import com.microel.speedtest.common.models.updateprovides.MeasureUpdateProvider;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.microel.speedtest.repositories.MeasureRepositoryDispatcher;

import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import reactor.core.publisher.Flux;

@Service
public class MeasurementSubscriptions implements GraphQLSubscriptionResolver {

    private final MeasureRepositoryDispatcher measureRepositoryDispatcher;

    private final Flux<BeginningMeasureUpdateProvider> beginningMeasureFlux;

    private final Flux<MeasureUpdateProvider> newMeasureFlux;

    private final Flux<ActiveSessionsUpdateProvider> updateActiveSessionFlux;

    public MeasurementSubscriptions(MeasureRepositoryDispatcher measureRepositoryDispatcher,
                                    Flux<BeginningMeasureUpdateProvider> beginningMeasureFlux,
                                    Flux<MeasureUpdateProvider> newMeasureFlux,
                                    Flux<ActiveSessionsUpdateProvider> updateActiveSessionFlux) {
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
        this.beginningMeasureFlux = beginningMeasureFlux;
        this.newMeasureFlux = newMeasureFlux;
        this.updateActiveSessionFlux = updateActiveSessionFlux;
    }

    public Publisher<BeginningMeasureUpdateProvider> updateBeginningMeasures(){
        return beginningMeasureFlux;
    }

    public Publisher<MeasureUpdateProvider> updateMeasures(){
        return newMeasureFlux;
    }

    public Publisher<ActiveSessionsUpdateProvider> updateActiveSession(){
        return updateActiveSessionFlux;
    }
}

package com.microel.speedtest.services.resolvers.subscription;

import com.microel.speedtest.common.models.updateprovides.ComplaintUpdateProvider;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ComplaintSubscription implements GraphQLSubscriptionResolver {

    private final Flux<ComplaintUpdateProvider> updateComplaintFlux;

    public ComplaintSubscription(Flux<ComplaintUpdateProvider> complaintUpdateProviderFlux) {
        this.updateComplaintFlux = complaintUpdateProviderFlux;
    }

    public Publisher<ComplaintUpdateProvider> updateComplaints(){
        return updateComplaintFlux;
    }
}

package com.microel.speedtest.services.resolvers.query;

import com.microel.speedtest.repositories.FeedbackRepositoryDispatcher;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class FeedbackQueries implements GraphQLQueryResolver {

    private final FeedbackRepositoryDispatcher feedbackRepositoryDispatcher;

    public FeedbackQueries(FeedbackRepositoryDispatcher feedbackRepositoryDispatcher) {
        this.feedbackRepositoryDispatcher = feedbackRepositoryDispatcher;
    }

    public Long getTotalFeedbacks() {
        return feedbackRepositoryDispatcher.count();
    }

    public Float getAvgAllFeedbacks() {
        return feedbackRepositoryDispatcher.avgAll();
    }
}

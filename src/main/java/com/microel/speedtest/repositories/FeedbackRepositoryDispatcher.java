package com.microel.speedtest.repositories;

import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.repositories.entities.Feedback;
import com.microel.speedtest.repositories.interfaces.FeedbackRepository;
import com.microel.speedtest.repositories.proxies.StringDoublePointProxy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class FeedbackRepositoryDispatcher {

    private final FeedbackRepository feedbackRepository;

    public FeedbackRepositoryDispatcher(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback save(Feedback feedback){
        return feedbackRepository.save(feedback);
    }

    public Boolean isAlreadyRated(Long sessionId, Timestamp after) {
        return feedbackRepository.existsBySession_sessionIdAndCreatedAfter(sessionId, after);
    }

    public Long count(){
        return feedbackRepository.count();
    }

    public Float avgAll(){
        List<Feedback> feedbacks = feedbackRepository.findAll();
        if(feedbacks.size() == 0) return 0f;
        return (float) feedbacks.stream().map(Feedback::getRate).reduce(Integer::sum).orElse(0) / (float) feedbacks.size();
    }

    public List<StringDoublePointProxy> getAvgInAddresses(TimeRange timeRange) {
        return feedbackRepository.avgGroupByAddress(timeRange.getStart().toString(),timeRange.getEnd().toString());
    }
}

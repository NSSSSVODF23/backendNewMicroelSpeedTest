package com.microel.speedtest.services.resolvers.query;

import com.microel.speedtest.common.models.QueryLimit;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.common.models.filters.ComplaintsFilter;
import com.microel.speedtest.common.models.filters.MatchingFactory;
import com.microel.speedtest.repositories.ComplaintRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.entities.Measure;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComplaintQueries implements GraphQLQueryResolver {

    private final ComplaintRepositoryDispatcher complaintRepositoryDispatcher;

    public ComplaintQueries(ComplaintRepositoryDispatcher complaintRepositoryDispatcher) {
        this.complaintRepositoryDispatcher = complaintRepositoryDispatcher;
    }

    public Page<Complaint> getComplaints(Complaint matchingObject, QueryLimit limits, TimeRange dateFilter, Boolean isProcessed) {
        return complaintRepositoryDispatcher.find(MatchingFactory.standardExample(matchingObject),dateFilter,isProcessed,MatchingFactory.paginator(limits, Sort.by(Sort.Direction.DESC,"created")));
    }

    public Integer getTotalComplaints(ComplaintsFilter filter) {
        return complaintRepositoryDispatcher.getComplaintCount(filter);
    }
}

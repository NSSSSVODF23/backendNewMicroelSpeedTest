package com.microel.speedtest.repositories;

import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.common.models.filters.ComplaintsFilter;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.interfaces.ComplaintRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ComplaintRepositoryDispatcher {

    private final ComplaintRepository complaintRepository;

    public ComplaintRepositoryDispatcher(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public List<Complaint> getComplaints(ComplaintsFilter filter){
        return complaintRepository.getComplaints(filter.getLogin(), filter.getAddress(), filter.getPhone(), filter.getProcessed(), filter.getIp(), filter.getMac(), filter.getStart(), filter.getEnd(), filter.getRows(), filter.getFirst());
    }

    public Integer getComplaintCount(ComplaintsFilter filter){
        return complaintRepository.getComplaintsCount(filter.getLogin(), filter.getAddress(), filter.getPhone(), filter.getProcessed(), filter.getIp(), filter.getMac(), filter.getStart(), filter.getEnd());
    }

    public Complaint getComplaint(Long id){
        return complaintRepository.findById(id).orElse(null);
    }

    public Complaint save(Complaint newComplaint) {
        return complaintRepository.save(newComplaint);
    }

    public Page<Complaint> find(Example<Complaint> example, TimeRange dateFilter, Boolean isProcessed, Pageable pageable) {
        return complaintRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(isProcessed != null) {
                if(isProcessed) predicates.add(criteriaBuilder.isNotNull(root.get("processed"))); else predicates.add(criteriaBuilder.isNull(root.get("processed")));
            }
            if(!dateFilter.isEmpty()) predicates.add(criteriaBuilder.between(root.get("created"),dateFilter.getStart(),dateFilter.getEnd()));
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root,criteriaBuilder,example));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        },pageable);
    }
}

package com.microel.speedtest.repositories.interfaces;

import com.microel.speedtest.repositories.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Boolean existsBySession_sessionIdAndCreatedAfter(Long sessionId, Timestamp created);
}

package com.microel.speedtest.repositories.interfaces;

import com.microel.speedtest.repositories.entities.Feedback;
import com.microel.speedtest.repositories.proxies.StringDoublePointProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Boolean existsBySession_sessionIdAndCreatedAfter(Long sessionId, Timestamp created);

    @Query(value = "SELECT address as x, avg(rate) as y FROM feedbacks as c JOIN acp_session ON f_session_id = session_id JOIN acp_house ON f_house_id = house_id WHERE c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x ORDER BY y DESC LIMIT 10", nativeQuery = true)
    List<StringDoublePointProxy> avgGroupByAddress(String start, String end);
}

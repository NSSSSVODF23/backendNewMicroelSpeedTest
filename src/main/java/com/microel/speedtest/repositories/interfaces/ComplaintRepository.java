package com.microel.speedtest.repositories.interfaces;

import com.microel.speedtest.common.models.chart.DateIntegerPoint;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.proxies.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>, JpaSpecificationExecutor<Complaint> {
    @Query(value = "SELECT * FROM complaints c JOIN acp_session a on c.f_session_id = a.session_id JOIN acp_house ah on ah.house_id = a.f_house_id LEFT JOIN users u on u.user_id = c.f_user_id " +
            "WHERE (:login IS NULL OR login ILIKE '%'||:login||'%') AND (:phone IS NULL OR c.phone ILIKE '%'||:phone||'%') AND (:ip IS NULL OR a.ip ILIKE '%'||:ip||'%') " +
            "AND (:mac IS NULL OR a.mac ILIKE '%'||:mac||'%') AND (:address IS NULL OR house_id = :address) AND (:processed IS NULL OR c.f_user_id ISNULL != :processed) " +
            "AND ((:start IS NULL AND :end IS NULL) OR c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp)) ORDER BY c.created DESC LIMIT :rows OFFSET :first",nativeQuery = true)
    List<Complaint> getComplaints(@Param("login") String login, @Param("address") Long address, @Param("phone") String phone, @Param("processed") Boolean processed,
                                          @Param("ip") String ip, @Param("mac") String mac, @Param("start") String start,
                                          @Param("end") String end, @Param("rows") Integer rows, @Param("first") Integer first);

    @Query(value = "SELECT count(complaint_id) FROM complaints c JOIN acp_session a on c.f_session_id = a.session_id JOIN acp_house ah on ah.house_id = a.f_house_id LEFT JOIN users u on u.user_id = c.f_user_id " +
            "WHERE (:login IS NULL OR login ILIKE '%'||:login||'%') AND (:phone IS NULL OR c.phone ILIKE '%'||:phone||'%') AND (:ip IS NULL OR a.ip ILIKE '%'||:ip||'%') " +
            "AND (:mac IS NULL OR a.mac ILIKE '%'||:mac||'%') AND (:address IS NULL OR house_id = :address) AND (:processed IS NULL OR c.f_user_id ISNULL != :processed) " +
            "AND ((:start IS NULL AND :end IS NULL) OR c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp))",nativeQuery = true)
    Integer getComplaintsCount(@Param("login") String login, @Param("address") Long address, @Param("phone") String phone, @Param("processed") Boolean processed,
                               @Param("ip") String ip, @Param("mac") String mac, @Param("start") String start,
                               @Param("end") String end);

    @Query(value = "SELECT date_trunc('day', c.created) as x, count(complaint_id) as y FROM complaints as c WHERE c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x", nativeQuery = true)
    List<DateIntegerPointProxy> getCountsInDate(@Param("start") String start, @Param("end") String end);

    @Query(value = "SELECT extract(dow from c.created) as x, count(complaint_id) as y FROM complaints as c WHERE c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x", nativeQuery = true)
    List<DOWIntegerPointProxy> getCountsInDay(@Param("start") String start, @Param("end") String end);

    @Query(value = "SELECT date_trunc('hour', c.created) as x, count(complaint_id) as y FROM complaints as c WHERE c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x", nativeQuery = true)
    List<DateIntegerPointProxy> getCountsInHour(@Param("start") String start, @Param("end") String end);

    @Query(value = "SELECT address as x, count(complaint_id) as y FROM complaints as c JOIN acp_session ON f_session_id = session_id JOIN acp_house ON f_house_id = house_id WHERE c.created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x ORDER BY y DESC LIMIT 10", nativeQuery = true)
    List<StringIntegerPointProxy> getCountsInAddresses(@Param("start") String start, @Param("end") String end);

    Page<Complaint> findAll(Specification specification, Pageable pageable);
}

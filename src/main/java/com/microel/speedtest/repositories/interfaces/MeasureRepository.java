package com.microel.speedtest.repositories.interfaces;

import java.sql.Timestamp;
import java.util.List;

import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.GroupStringCTypeIntegerPointProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.Measure;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long>, JpaSpecificationExecutor {
    @Query(value = "SELECT * FROM measures JOIN acp_session ON session_id = f_session_id JOIN acp_house ON house_id = f_house_id " +
            "WHERE (:login IS NULL OR login ILIKE '%'||:login||'%') AND (:address IS NULL OR acp_house.house_id = :address) " +
            "AND (:ip IS NULL OR ip ILIKE '%'||:ip||'%') AND (:mac IS NULL OR mac ILIKE '%'||:mac||'%') " +
            "AND ((:start IS NULL AND :end IS NULL) OR created BETWEEN cast(:start AS timestamp) " +
            "AND cast(:end AS timestamp)) ORDER BY created DESC LIMIT :rows OFFSET :first", nativeQuery = true)
    List<Measure> getFilteredMeasures(@Param("login") String login, @Param("address") Long address,
                                             @Param("ip") String ip, @Param("mac") String mac, @Param("start") String start,
                                             @Param("end") String end, @Param("rows") Integer rows, @Param("first") Integer first);

    @Query(value = "SELECT count(measure_id) FROM measures JOIN acp_session ON session_id = f_session_id JOIN acp_house ON house_id = f_house_id " +
            "WHERE (:login IS NULL OR login ILIKE '%'||:login||'%') AND (:address IS NULL OR acp_house.house_id = :address) AND " +
            "(:ip IS NULL OR ip ILIKE '%'||:ip||'%') AND (:mac IS NULL OR mac ILIKE '%'||:mac||'%') AND " +
            "((:start IS NULL AND :end IS NULL) OR created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp))", nativeQuery = true)
    Integer getNumberOfFilteredMeasures(@Param("login") String login, @Param("address") Long address,
                                               @Param("ip") String ip, @Param("mac") String mac, @Param("start") String start,
                                               @Param("end") String end);
    @Query(value = "SELECT date_trunc('day', created) as x, connection_type as g, count(measure_id) as y FROM measures WHERE created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x, g", nativeQuery = true)
    List<GroupDayCTypeIntegerPointProxy> getCountsInDays(@Param("start") String start, @Param("end") String end);

    @Query(value = "SELECT address as x, connection_type as g, count(measure_id) as y FROM measures JOIN acp_session ON f_session_id = session_id JOIN acp_house ON f_house_id = house_id WHERE created BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp) GROUP BY x, g ORDER BY y DESC LIMIT 10", nativeQuery = true)
    List<GroupStringCTypeIntegerPointProxy> getCountsInAddresses(@Param("start") String start, @Param("end") String end);

    Integer countByCreatedBetween(Timestamp start, Timestamp end);
}

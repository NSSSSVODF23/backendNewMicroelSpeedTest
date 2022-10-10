package com.microel.speedtest.repositories.entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

import javax.persistence.*;

import org.hibernate.annotations.TypeDef;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.common.types.SpeedChartPoint;
import com.microel.speedtest.repositories.types.SpeedChartPointType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "measures")
@TypeDef(name = "speedChartPoint", typeClass = SpeedChartPointType.class, defaultForType = SpeedChartPoint.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measureId;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_device_id")
    private Device device;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_session_id")
    private AcpSession session;
    @Column(columnDefinition = "timestamp default now()")
    private Timestamp created;
    @Column(columnDefinition = "integer default 1")
    private MeasureConnectionTypes connectionType;
    private Float ping;
    private Float pingJitter;
    private Float downloadSpeed;
    private Float downloadSpeedUser;
    private Float downloadStability;
    private Float downloadLoss;
    private SpeedChartPoint[] downloadSpeedChart;
    private SpeedChartPoint[] downloadSpeedChartUser;
    private Float uploadSpeed;
    private Float uploadSpeedUser;
    private Float uploadStability;
    private Float uploadLoss;
    private SpeedChartPoint[] uploadSpeedChart;
    private SpeedChartPoint[] uploadSpeedChartUser;
    @Column(columnDefinition = "boolean default false")
    private Boolean isUsed;
    @Column(columnDefinition = "boolean default false")
    private Boolean isFailed;
    @Transient
    @Builder.Default
    private Boolean isStarted = false;

    public void setResult(Measure result) {
        ping = result.getPing();
        pingJitter = result.getPingJitter();

        downloadSpeed = result.getDownloadSpeed();
        downloadSpeedUser = result.getDownloadSpeedUser();
        downloadStability = result.getDownloadStability();
        downloadLoss = result.getDownloadLoss();
        downloadSpeedChart = result.getDownloadSpeedChart();
        downloadSpeedChartUser = result.getDownloadSpeedChartUser();

        uploadSpeed = result.getUploadSpeed();
        uploadSpeedUser = result.getUploadSpeedUser();
        uploadStability = result.getUploadStability();
        uploadLoss = result.getUploadLoss();
        uploadSpeedChart = result.getUploadSpeedChart();
        uploadSpeedChartUser = result.getUploadSpeedChartUser();
    }

    public void startMeasure() {
        created = Timestamp.from(Instant.now());
        isStarted = true;
    }

    /**
     * Копирует текущий объект, но не полностью, а только информацию о сессии и
     * девайсе.
     */
    public Measure copy() {
        return Measure.builder()
                .device(device)
                .session(session)
                .build();
    }

    @Override
    public String toString() {
        return "Measure{" +
                "measureId=" + measureId +
                ", device=" + device +
                ", session=" + session +
                ", created=" + created +
                ", connectionType=" + connectionType +
                ", ping=" + ping +
                ", pingJitter=" + pingJitter +
                ", downloadSpeed=" + downloadSpeed +
                ", downloadStability=" + downloadStability +
                ", downloadLoss=" + downloadLoss +
                ", downloadSpeedChart=" + Arrays.toString(downloadSpeedChart) +
                ", uploadSpeed=" + uploadSpeed +
                ", uploadStability=" + uploadStability +
                ", uploadLoss=" + uploadLoss +
                ", uploadSpeedChart=" + Arrays.toString(uploadSpeedChart) +
                ", isUsed=" + isUsed +
                ", isFailed=" + isFailed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Measure measure = (Measure) o;

        return measureId.equals(measure.measureId);
    }

    @Override
    public int hashCode() {
        return measureId.hashCode();
    }

    public boolean hasDeviceInfo(){
        return device != null;
    }

    public boolean hasSession(){
        return session != null;
    }
}

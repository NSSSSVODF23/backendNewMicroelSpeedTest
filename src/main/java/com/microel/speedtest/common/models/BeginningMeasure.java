package com.microel.speedtest.common.models;

import com.microel.speedtest.repositories.entities.Measure;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class BeginningMeasure extends Measure {
    private String beginningId;
    public BeginningMeasure(@NotNull Measure measure, String id){
        super();
        setMeasureId(measure.getMeasureId());
        setDevice(measure.getDevice());
        setSession(measure.getSession());
        setCreated(measure.getCreated());
        setConnectionType(measure.getConnectionType());

        setPing(measure.getPing());
        setPingJitter(measure.getPingJitter());

        setDownloadSpeed(measure.getDownloadSpeed());
        setDownloadStability(measure.getDownloadStability());
        setDownloadLoss(measure.getDownloadLoss());
        setDownloadSpeedChart(measure.getDownloadSpeedChart());

        setUploadSpeed(measure.getUploadSpeed());
        setUploadStability(measure.getUploadStability());
        setUploadLoss(measure.getUploadLoss());
        setUploadSpeedChart(measure.getUploadSpeedChart());

        setIsUsed(measure.getIsUsed());
        setIsFailed(measure.getIsFailed());
        setIsStarted(measure.getIsStarted());
        setBeginningId(id);
    }
}

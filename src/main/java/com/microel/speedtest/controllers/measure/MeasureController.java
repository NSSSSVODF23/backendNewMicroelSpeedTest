package com.microel.speedtest.controllers.measure;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.common.models.*;
import com.microel.speedtest.common.models.updateprovides.ActiveSessionsUpdateProvider;
import com.microel.speedtest.common.models.updateprovides.BeginningMeasureUpdateProvider;
import com.microel.speedtest.common.models.updateprovides.DeviceUpdateProvider;
import com.microel.speedtest.repositories.entities.AcpSession;
import com.microel.speedtest.repositories.entities.Device;
import com.microel.speedtest.services.websocket.handler.MeasureHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microel.speedtest.common.enums.MeasureActionTypes;
import com.microel.speedtest.repositories.DeviceRepositoryDispatcher;
import com.microel.speedtest.repositories.MeasureRepositoryDispatcher;
import com.microel.speedtest.repositories.SessionRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Measure;
import com.microel.speedtest.services.acpparser.Acp;
import com.microel.speedtest.services.websocket.transport.MeasureActionMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class MeasureController {

    private final Map<WebSocketSession, Measure> measurements = new HashMap<>();// Текущие измерения запущенные пользователями

    private final Acp acp; // Класс для работы с АЦП

    private final DeviceRepositoryDispatcher deviceRepositoryDispatcher; // Репозиторий для работы с девайсами

    private final MeasureRepositoryDispatcher measureRepositoryDispatcher; // Класс для публикации замеров

    private final SessionRepositoryDispatcher sessionRepositoryDispatcher;

    private final Sinks.Many<BeginningMeasureUpdateProvider> beginningMeasureSink;

    private final Sinks.Many<ActiveSessionsUpdateProvider> updateActiveSessionSink;

    public MeasureController(Acp acp, DeviceRepositoryDispatcher deviceRepositoryDispatcher, MeasureRepositoryDispatcher measureRepositoryDispatcher,
                             SessionRepositoryDispatcher sessionRepositoryDispatcher, Sinks.Many<BeginningMeasureUpdateProvider> beginningMeasureSink, Sinks.Many<ActiveSessionsUpdateProvider> activeSessionsUpdateSink, Sinks.Many<DeviceUpdateProvider> updateDeviceSink) {
        this.acp = acp;
        this.deviceRepositoryDispatcher = deviceRepositoryDispatcher;
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
        this.sessionRepositoryDispatcher = sessionRepositoryDispatcher;
        this.beginningMeasureSink = beginningMeasureSink;
        this.updateActiveSessionSink = activeSessionsUpdateSink;
    }

    @Scheduled(fixedRate = 5000)
    public synchronized void measureKeeper() {
        List<WebSocketSession> removeSessions = measurements.entrySet().stream().filter(e -> !e.getKey().isOpen() ||
                (e.getValue().getIsStarted() &&
                        e.getValue().getCreated().toInstant().plusSeconds(60).isBefore(Instant.now()))).map(Map.Entry::getKey).collect(Collectors.toList());
        removeSessions.forEach(session -> {
            updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.DELETE, new ActiveSession(measurements.get(session), session)));
            beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.DELETE, new BeginningMeasure(measurements.get(session), session.getId())));
            measurements.remove(session);
        });
    }

    public MeasureActionMessage handleActionMessage(MeasureActionMessage message, WebSocketSession session) {
        switch (message.getType()) {
            case DEVICE_INFO:
                final Measure measure = measurements.get(session);
                final Device receivedDevice = message.getDeviceInfo();
                if (receivedDevice != null) {
                    receivedDevice.setIp(MeasureHandler.getRemoteIp(session));
                    receivedDevice.setHostname(Objects.requireNonNull(session.getRemoteAddress()).getHostName());
                    measure.setDevice(deviceRepositoryDispatcher.updateDeviceInfo(receivedDevice));
                    acp.takeMeasureSession(MeasureHandler.getRemoteIp(session), (acpSession) -> {
                        final AcpSession savedSession = sessionRepositoryDispatcher.save(acpSession);

                        measure.setSession(savedSession);
                        measure.getDevice().setLastSession(savedSession);

                        deviceRepositoryDispatcher.updateSession(measure.getDevice());

                        updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.UPDATE, new ActiveSession(measure, session)));
                        beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.UPDATE, new BeginningMeasure(measure, session.getId())));
                    });
                    try {
                        session.sendMessage(new TextMessage(new MeasureActionMessage(MeasureActionTypes.TESTING_MODE, measure.getDevice().getIsPro()).toString()));
                    } catch (IOException e) {
                        log.warn(e.getMessage());
                    }
                    updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.UPDATE, new ActiveSession(measure, session)));
                }
                return null;
            case START:
                try {
                    final Measure running = measurements.get(session);
                    running.setConnectionType(message.getConnectionType());
                    running.startMeasure();
                    beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.ADD, new BeginningMeasure(running, session.getId())));
                    updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.UPDATE, new ActiveSession(running, session)));
                } catch (Exception e) {
                    log.error("No prepared measurement found for device: " + message.getDeviceInfo().getDeviceId());
                }
                return null;
            case END:
                try {
                    Measure result = message.getResult();
                    Measure ended = measurements.get(session);
                    if(ended == null) return null;
                    ended.setIsStarted(false);
                    ended.setResult(result);
                    if(ended.getConnectionType() == null) ended.setConnectionType(MeasureConnectionTypes.WIFI);
                    if(ended.getCreated() == null) ended.setCreated(Timestamp.from(Instant.now().minus(30, ChronoUnit.SECONDS)));
                    Measure savedMeasure = measureRepositoryDispatcher.save(ended);
                    MeasureActionMessage response = new MeasureActionMessage();
                    response.setType(MeasureActionTypes.RESULT);
                    response.setResultId(savedMeasure.getMeasureId());
                    measurements.put(session, ended.copy());
                    beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.DELETE, new BeginningMeasure(ended, session.getId())));
                    updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.UPDATE, new ActiveSession(ended, session)));
                    return response;
                } catch (NoSuchElementException e) {
                    log.error("No ended measurement found for device: " + message.getDeviceInfo().getDeviceId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            case ERROR:
            case RESULT:
                return null;
            case ABORT:
                Measure aborted = measurements.get(session);
                aborted.setIsStarted(false);
                beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.DELETE, new BeginningMeasure(aborted, session.getId())));
                updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.UPDATE, new ActiveSession(aborted, session)));
                return null;
        }
        return null;
    }

    public void handleConnect(WebSocketSession session) throws IOException {
        final Measure created = new Measure();
        measurements.put(session, created);

        session.sendMessage(new TextMessage(new MeasureActionMessage(MeasureActionTypes.GET_DEVICE_INFO).toString()));
        updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.ADD, new ActiveSession(created, session)));
    }

    public void handleDisconnect(WebSocketSession session) {
        Measure activeSession = measurements.remove(session);
        updateActiveSessionSink.tryEmitNext(new ActiveSessionsUpdateProvider(ListMutationTypes.DELETE, new ActiveSession(activeSession, session)));
        beginningMeasureSink.tryEmitNext(new BeginningMeasureUpdateProvider(ListMutationTypes.DELETE, new BeginningMeasure(activeSession, session.getId())));
    }

    public List<BeginningMeasure> getBeginningMeasures() {
        return measurements.entrySet().stream().filter(entry -> entry.getValue().getIsStarted()).
                map(entry -> new BeginningMeasure(entry.getValue(), entry.getKey().getId())).
                collect(Collectors.toList());
    }

    public boolean isMeasureExists() {
        return true;
    }

    public List<ActiveSession> getActiveSessions() {
        return measurements.entrySet().stream().map(entry -> new ActiveSession(entry.getValue(), entry.getKey())).collect(Collectors.toList());
    }
}

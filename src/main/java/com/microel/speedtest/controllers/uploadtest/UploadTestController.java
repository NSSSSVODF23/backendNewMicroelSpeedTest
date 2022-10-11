package com.microel.speedtest.controllers.uploadtest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.services.websocket.handler.UploadResolverHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadTestController {
    static final Integer BEACON_INTERVAL = 150;
    private final Map<String, Long> uploadBytesByUser = new HashMap<>();
    private final UploadResolverHandler uploadResolverHandler;
    private final Map<String, SpeedResponseWorker> workers = new HashMap<>();

    public UploadTestController(UploadResolverHandler uploadResolverHandler) {
        this.uploadResolverHandler = uploadResolverHandler;
        uploadResolverHandler.addConnectHandler((dId) -> {
            log.info("Connected to device: " + dId);
            runTest(dId);
        });
    }

    public class SpeedResponseWorker implements Runnable {

        Boolean running = false;
        Instant initial;
        Integer index = 0;
        String deviceId;

        public SpeedResponseWorker(String deviceId) {
            this.deviceId = deviceId;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(BEACON_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!running)
                    break;
                Long uploadingBytes = uploadBytesByUser.get(deviceId);
                if (uploadingBytes == null) {
                    throw new RuntimeException("No bytes uploaded for device: " + deviceId);
                }
                if (index == 0) {
                    initial = Instant.now();
                }
                if (uploadingBytes > 0) {
                    uploadResolverHandler.sendSpeedResponse(deviceId, uploadingBytes, initial, index);
                    index++;
                }
            }
            workers.remove(deviceId);
            uploadBytesByUser.remove(deviceId);
        }

    }

    public void stopWorker(String deviceId) {
        SpeedResponseWorker worker = workers.get(deviceId);
        if (worker == null) {
            return;
        }
        worker.running = false;
        workers.remove(deviceId);
    }

    synchronized public void runTest(String deviceId) {
        if (!workers.containsKey(deviceId)) {
            uploadBytesByUser.put(deviceId, 0L);
            SpeedResponseWorker worker = new SpeedResponseWorker(deviceId);
            workers.put(deviceId, worker);
            worker.running = true;
            new Thread(worker).start();
            uploadResolverHandler.addDisconnectHandler(deviceId, this::stopWorker);
        }
    }

    synchronized public void addBytes(String deviceId, long bytes) {
        // Get worker and check if it is running
        SpeedResponseWorker worker = workers.get(deviceId);
        if (worker != null && worker.running) {
            uploadBytesByUser.put(deviceId, uploadBytesByUser.get(deviceId) + bytes);
        }
    }
}

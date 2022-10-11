package com.microel.speedtest.services.resolvers.web;

import com.microel.speedtest.controllers.measure.MeasureController;
import com.microel.speedtest.controllers.uploadtest.UploadTestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Controller
@RequestMapping("public")
public class SpeedTestResolver {

    private final UploadTestController uploadTestController;
    private final MeasureController measureController;

    public SpeedTestResolver(UploadTestController uploadTestController, MeasureController measureController) {
        this.uploadTestController = uploadTestController;
        this.measureController = measureController;
    }

    @PostMapping(value = "download")
    public void download(HttpServletResponse response, @RequestParam String deviceId) throws IOException {
        response.setHeader("Content-Type","application/octet-stream");
        response.setHeader("Content-Length","50000000");
        if (deviceId == null || deviceId.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }
        response.setStatus(HttpStatus.OK.value());
        OutputStream os = response.getOutputStream();
        final byte[] bytes = new byte[50_000_000];
        os.write(bytes);
        os.close();
    }

    @PostMapping("upload")
    @ResponseStatus(HttpStatus.OK)
    public void upload(HttpServletResponse response, InputStream is, @RequestParam String deviceId) throws IOException {
        if (deviceId == null || deviceId.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            is.close();
        }else{
            int b;
            while ((b = is.read(new byte[4_096])) != -1) {
                uploadTestController.addBytes(deviceId, b);
            }
        }
        is.close();
    }



    /**
     * Проверяем подготовлен ли текущий сеанс
     */
    @PostMapping("check")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> check() {
        try {
            if (measureController.isMeasureExists()) {
                return ResponseEntity.ok(true);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(false);
    }
}

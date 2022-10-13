package com.microel.speedtest.services.resolvers.web;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.models.*;
import com.microel.speedtest.common.models.bodies.*;
import com.microel.speedtest.common.models.updateprovides.ComplaintUpdateProvider;
import com.microel.speedtest.controllers.measure.MeasureController;
import com.microel.speedtest.repositories.*;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.entities.Device;
import com.microel.speedtest.repositories.entities.Feedback;
import com.microel.speedtest.services.CaptchaService;
import com.microel.speedtest.services.acpparser.Acp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.microel.speedtest.repositories.entities.Measure;
import reactor.core.publisher.Sinks;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Controller
@RequestMapping("public")
public class PublicResolver {

    private final MeasureRepositoryDispatcher measureRepositoryDispatcher;
    private final ComplaintRepositoryDispatcher complaintRepositoryDispatcher;
    private final FeedbackRepositoryDispatcher feedbackRepositoryDispatcher;
    private final DeviceRepositoryDispatcher deviceRepositoryDispatcher;
    private final CaptchaService captchaService;
    private final Sinks.Many<ComplaintUpdateProvider> updateComplaintSink;
    private final Acp acp;

    private final MeasureController measureController;

    public PublicResolver(MeasureRepositoryDispatcher measureRepositoryDispatcher, ComplaintRepositoryDispatcher complaintRepositoryDispatcher, SessionRepositoryDispatcher sessionRepositoryDispatcher, DeviceRepositoryDispatcher deviceRepositoryDispatcher, CaptchaService captchaService, Sinks.Many<ComplaintUpdateProvider> updateComplaintSink, FeedbackRepositoryDispatcher feedbackRepositoryDispatcher, Acp acp, MeasureController measureController) {
        this.measureRepositoryDispatcher = measureRepositoryDispatcher;
        this.complaintRepositoryDispatcher = complaintRepositoryDispatcher;
        this.deviceRepositoryDispatcher = deviceRepositoryDispatcher;
        this.captchaService = captchaService;
        this.feedbackRepositoryDispatcher = feedbackRepositoryDispatcher;
        this.updateComplaintSink = updateComplaintSink;
        this.acp = acp;
        this.measureController = measureController;
    }

    @PostMapping("measure")
    public ResponseEntity<PublicApiResponse> getMeasureById(@RequestBody GettingMeasureBody body,HttpServletRequest request) {
        if (body.isWrong()) return ResponseEntity.ok(PublicApiResponse.wrongRequest());

        final Measure measure = measureRepositoryDispatcher.findById(body.getId());

        if (measure == null) {
            return ResponseEntity.ok(PublicApiResponse.error("Замер не найден"));
        }

        measure.setDevice(null);
        measure.setSession(null);

        return ResponseEntity.ok(PublicApiResponse.ok(measure));
    }

    @PostMapping("old-measures")
    public ResponseEntity<PublicApiResponse> getOldMeasures(HttpServletRequest request){
        try {
            CountDownLatch downLatch = new CountDownLatch(1);
            AtomicReference<Page<Measure>> measures = new AtomicReference<>();
            acp.takeMeasureSession(request.getRemoteAddr(), session -> {
                if(session.getLogin() ==null || session.getLogin().equals("Без логина")){
                    downLatch.countDown();
                    return;
                }

                measures.set(measureRepositoryDispatcher.findByLoginLastTen(session.getLogin()));

                downLatch.countDown();
            });

            downLatch.await(30, TimeUnit.SECONDS);

            return ResponseEntity.ok(PublicApiResponse.ok(measures.get().map(measure -> { // От бесконечной петли
                measure.getSession().setHouse(null);
                measure.getSession().setMeasures(null);
                measure.getDevice().setMeasures(null);
                measure.getDevice().setLastSession(null);
                return measure;
            })));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("complaint")
    public ResponseEntity<PublicApiResponse> createComplaint(@RequestBody CreateComplaintBody body, HttpServletRequest request) {
        if (body.isWrong()) return ResponseEntity.ok(PublicApiResponse.wrongRequest());
        if (body.getCaptchaToken() == null || !captchaService.validate(body.getCaptchaToken()))return ResponseEntity.ok(PublicApiResponse.error("Подтвердите что вы не робот."));
        final Complaint newComplaint = new Complaint();
        newComplaint.setDescription(body.getDescription());
        newComplaint.setPhone(body.getPhone());
        acp.takeMeasureSession(request.getRemoteAddr(), session -> {
            newComplaint.setSession(session);
            newComplaint.setCreated(Timestamp.from(Instant.now()));

            updateComplaintSink.tryEmitNext(new ComplaintUpdateProvider(ListMutationTypes.ADD, complaintRepositoryDispatcher.save(newComplaint)));
        });

        return ResponseEntity.ok(PublicApiResponse.ok(null));
    }

    @PutMapping("rating")
    public ResponseEntity<PublicApiResponse> createFeedback(@RequestBody CreateFeedbackBody body, HttpServletRequest request) {
        if (body.isWrong())return ResponseEntity.ok(PublicApiResponse.wrongRequest());

        final Feedback newFeedback = new Feedback();
        newFeedback.setRate(body.getRating());
        acp.takeMeasureSession(request.getRemoteAddr(), session -> {
            newFeedback.setSession(session);
            newFeedback.setCreated(Timestamp.from(Instant.now()));

            feedbackRepositoryDispatcher.save(newFeedback);
        });

        return ResponseEntity.ok(PublicApiResponse.ok(null));
    }

    @PostMapping("has-rated")
    public ResponseEntity<PublicApiResponse> checkIsRated(HttpServletRequest request) {
        try {
            CountDownLatch downLatch = new CountDownLatch(1);
            AtomicBoolean hasRated = new AtomicBoolean(false);
            acp.takeMeasureSession(request.getRemoteAddr(), session -> {
                Timestamp startOfDay = Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN));
                hasRated.set(feedbackRepositoryDispatcher.isAlreadyRated(session.getSessionId(), startOfDay));
                downLatch.countDown();
            });
            downLatch.await(30, TimeUnit.SECONDS);
            if (hasRated.get()) {
                return ResponseEntity.ok(PublicApiResponse.error("Уже есть оценка"));
            } else {

                return ResponseEntity.ok(PublicApiResponse.ok(null));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("is-pro")
    public ResponseEntity<PublicApiResponse> checkIsPro(@RequestBody DeviceIdBody body) {
        Device device = deviceRepositoryDispatcher.findById(body.getDeviceId());
        if(device == null) return ResponseEntity.ok(PublicApiResponse.error("Устройство не найдено"));
        return ResponseEntity.ok(PublicApiResponse.ok(device.getIsPro()));
    }

    @PostMapping("already-run")
    public ResponseEntity<PublicApiResponse> checkIsAlreadyRun(HttpServletRequest request){
        Measure measure = measureController.getBeginningMeasures().stream().filter(beginningMeasure -> {
            log.info(beginningMeasure.getSession().getIp()+" "+request.getRemoteAddr());
            return beginningMeasure.getSession().getIp().equals(request.getRemoteAddr());
        }).findFirst().orElse(null);
        if(measure == null) return ResponseEntity.ok(PublicApiResponse.ok(null));
        return ResponseEntity.ok(PublicApiResponse.error("Тестирование запущенно на вашем устройстве, дождитесь его окончания."));
    }
}

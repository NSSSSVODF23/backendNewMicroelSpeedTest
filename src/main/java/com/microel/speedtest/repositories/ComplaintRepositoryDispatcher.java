package com.microel.speedtest.repositories;

import com.microel.speedtest.common.exceptions.CustomGraphqlException;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.common.models.filters.ComplaintsFilter;
import com.microel.speedtest.controllers.tlg.ComplaintBot;
import com.microel.speedtest.repositories.entities.Complaint;
import com.microel.speedtest.repositories.entities.User;
import com.microel.speedtest.repositories.interfaces.ComplaintRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class ComplaintRepositoryDispatcher {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final ComplaintRepository complaintRepository;
    private final ComplaintBot complaintBot;

    public ComplaintRepositoryDispatcher(ComplaintRepository complaintRepository, ComplaintBot complaintBot) {
        this.complaintRepository = complaintRepository;
        this.complaintBot = complaintBot;
    }

    public List<Complaint> getComplaints(ComplaintsFilter filter){
        return complaintRepository.getComplaints(filter.getLogin(), filter.getAddress(), filter.getPhone(), filter.getProcessed(), filter.getIp(), filter.getMac(), filter.getStart(), filter.getEnd(), filter.getRows(), filter.getFirst());
    }

    public Integer getComplaintCount(ComplaintsFilter filter){
        return complaintRepository.getComplaintsCount(filter.getLogin(), filter.getAddress(), filter.getPhone(), filter.getProcessed(), filter.getIp(), filter.getMac(), filter.getStart(), filter.getEnd());
    }

    public Complaint getComplaint(Long id){
        return complaintRepository.findById(id).orElse(null);
    }

    public Complaint save(Complaint newComplaint) {
        final Complaint savedComplaint = complaintRepository.save(newComplaint);

        SendMessage complaintNotification = new SendMessage();
        complaintNotification.setChatId("-1001844526159");
        complaintNotification.enableHtml(true);
        complaintNotification.setText("⚡️ <u>Обращение № <b>"+savedComplaint.getComplaintId()+"</b></u> <i>"+savedComplaint.getCreated().toLocalDateTime().format(formatter)+"</i>\n" +
                "\n" +
                "       Логин: <b>"+savedComplaint.getSession().getLogin()+"</b>\n" +
                "       Адрес: <b>"+savedComplaint.getSession().getHouse().getAddress()+"</b>\n" +
                "       Телефон: <b>"+savedComplaint.getPhone()+"</b>\n" +
                "\n" +
                "       Описание: "+savedComplaint.getDescription());
        try {
            complaintBot.execute(complaintNotification);
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить оповещение о новом обращении.");
            log.error(e.getLocalizedMessage());
        }

        return savedComplaint;
    }

    public Page<Complaint> find(Example<Complaint> example, TimeRange dateFilter, Boolean isProcessed, Pageable pageable) {
        return complaintRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(isProcessed != null) {
                if(isProcessed) predicates.add(criteriaBuilder.isNotNull(root.get("processed"))); else predicates.add(criteriaBuilder.isNull(root.get("processed")));
            }
            if(!dateFilter.isEmpty()) predicates.add(criteriaBuilder.between(root.get("created"),dateFilter.getStart(),dateFilter.getEnd()));
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root,criteriaBuilder,example));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        },pageable);
    }

    public Complaint doProcessed(Long id, User user) throws CustomGraphqlException {
        final Complaint complaint = getComplaint(id);
        if (complaint == null) throw new CustomGraphqlException("Жалоба не найдена");
        complaint.setProcessed(user);
        complaint.setProcessedTime(Timestamp.from(Instant.now()));
        final Complaint saved = complaintRepository.save(complaint);
        SendMessage complaintNotification = new SendMessage();
        complaintNotification.setChatId("-1001844526159");
        complaintNotification.enableHtml(true);
        complaintNotification.setText("✅ <u>Обращение № <b>"+saved.getComplaintId()+"</b></u> обработано пользователем <b>"+user.getName()+"</b> <i>"+saved.getProcessedTime().toLocalDateTime().format(formatter)+"</i>");
        try {
            complaintBot.execute(complaintNotification);
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить оповещение о обработанном обращении.");
            log.error(e.getLocalizedMessage());
        }
        return saved;
    }
}

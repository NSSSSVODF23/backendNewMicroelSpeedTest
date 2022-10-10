package com.microel.speedtest.repositories.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private Integer rate;
    @Column(columnDefinition = "timestamp default now()")
    private Timestamp created;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_session_id")
    private AcpSession session;
}

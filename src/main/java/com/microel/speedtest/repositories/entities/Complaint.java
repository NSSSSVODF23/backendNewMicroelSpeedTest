package com.microel.speedtest.repositories.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintId;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private Timestamp created;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_session_id")
    private AcpSession session;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_user_id")
    private User processed;
    private Timestamp processedTime;
}

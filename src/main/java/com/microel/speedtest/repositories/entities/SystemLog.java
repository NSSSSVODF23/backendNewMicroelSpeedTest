package com.microel.speedtest.repositories.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "system_logs")
@Getter
@Setter
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long logId;
    public Timestamp stamp;
    public String description;
}

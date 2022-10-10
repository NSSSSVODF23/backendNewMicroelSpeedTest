package com.microel.speedtest.repositories.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;

@Entity
@Table(name = "network_util")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworkUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long networkUtilId;
    private Timestamp stamp;
    private Double rx;
    private Double tx;
}

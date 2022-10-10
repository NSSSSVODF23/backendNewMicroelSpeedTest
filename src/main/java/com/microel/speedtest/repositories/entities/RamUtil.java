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
@Table(name = "ram_util")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RamUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ramUtilId;
    private Timestamp stamp;
    private Long utilized;
    private Long total;
}

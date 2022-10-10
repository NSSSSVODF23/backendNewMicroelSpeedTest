package com.microel.speedtest.repositories.entities;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "acp_session", uniqueConstraints = { @UniqueConstraint(columnNames = { "mac", "login", "ip" }) })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcpSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    @Column(columnDefinition = "varchar(255) default '00:00:00:00:00:00'")
    private String mac;
    @Column(columnDefinition = "varchar(255) default 'Без логина'")
    private String login;
    @Column(columnDefinition = "varchar(255) default '0.0.0.0'")
    private String ip;
    @Column(columnDefinition = "smallint default 0")
    private Short vlan;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "f_house_id")
    private AcpHouse house;

    @OneToMany(mappedBy = "session", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Measure> measures;

    @Override
    public String toString() {
        return "AcpSession{" +
                "sessionId=" + sessionId +
                ", mac='" + mac + '\'' +
                ", login='" + login + '\'' +
                ", ip='" + ip + '\'' +
                ", house=" + house +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AcpSession acpSession = (AcpSession) o;

        return mac.equals(acpSession.mac) && login.equals(acpSession.login) && ip.equals(acpSession.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mac, login, house);
    }
}

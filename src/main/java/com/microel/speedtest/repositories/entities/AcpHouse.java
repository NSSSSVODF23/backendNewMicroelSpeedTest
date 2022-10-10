package com.microel.speedtest.repositories.entities;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.*;

@Entity
@Table(name = "acp_house", uniqueConstraints = { @UniqueConstraint(columnNames = { "address", "vlan" }) })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcpHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;
    private Short vlan;
    private String address;

    @OneToMany(mappedBy = "house")
    private Set<AcpSession> sessions;

    public AcpHouse(Long id, Short vlan, String address) {
        this.houseId = id;
        this.vlan = vlan;
        this.address = address;
    }

    @Override
    public String toString() {
        return "AcpHouse{" +
                "houseId=" + houseId +
                ", vlan=" + vlan +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AcpHouse acpHouse = (AcpHouse) o;

        return vlan.equals(acpHouse.vlan) && address.equals(acpHouse.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vlan, address);
    }
}

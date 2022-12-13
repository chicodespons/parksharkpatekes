package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Member {
    @Id
    @SequenceGenerator(name="member_seq", sequenceName = "member_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    private long id;
    @Embedded
    private Name name;
    private String phoneNumber;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private MembershipLvl membershipLvl;
    @OneToOne
    private LicensePlate licensePlate;
    @CreationTimestamp
    private Timestamp registrationDate;
    @OneToOne
    private Address address;
}

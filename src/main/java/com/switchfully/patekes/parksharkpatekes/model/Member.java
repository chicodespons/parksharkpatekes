package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    private LocalDateTime registrationDate;
    @OneToOne
    private Address address;

    public Member(Name name, String phoneNumber, String email, String password, MembershipLvl membershipLvl, LicensePlate licensePlate,Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.membershipLvl = membershipLvl;
        this.licensePlate = licensePlate;
        this.registrationDate = LocalDateTime.now();
        this.address = address;
    }

    public Member() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(name, member.name) && Objects.equals(phoneNumber, member.phoneNumber) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && membershipLvl == member.membershipLvl && Objects.equals(licensePlate, member.licensePlate) && Objects.equals(address, member.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, email, password, membershipLvl, licensePlate, address);
    }
}

package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Data
public class ContactPerson {
    @Id
    @SequenceGenerator(name="contact_person_seq", sequenceName = "contact_person_seq", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_person_seq")
    private Long id;
    @Embedded
    private Name name;
    private int mobilePhoneNumber;
    private int telephonePhoneNumber;
    private String email;
    private Address address;
}

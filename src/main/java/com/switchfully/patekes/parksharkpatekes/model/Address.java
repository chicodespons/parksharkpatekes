package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Data
public class Address {
    @Id
    @SequenceGenerator(name="address_seq", sequenceName = "address_seq", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    private long id;
    private String streetName;
    private int number;
    @OneToOne
    private PostalCode postalCode;
}

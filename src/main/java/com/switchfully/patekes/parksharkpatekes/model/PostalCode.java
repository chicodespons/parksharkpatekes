package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PostalCode {
    @Id
    @SequenceGenerator(name="postalcode_seq", sequenceName = "postalcode_seq", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postalcode_seq")
    private long id;
    private int actualPostalCode;
    private City cityLabel; // to be an enum
}

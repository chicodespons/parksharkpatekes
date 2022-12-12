package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Data
public class ParkingLot {
    @Id
    @SequenceGenerator(name="parkinglot_seq", sequenceName = "parkinglot_seq", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parkinglot_seq")
    private long id;
    @ManyToOne
    @JoinColumn(name = "id")
    private Division fk_division_id;
    @Embedded
    private Name name;
    @OneToOne
    private ContactPerson contactPerson;
    @OneToOne
    private Address address;
    private final int MAX_CAPACITY;
    private Category category;
    private final int PRICE_PER_HOUR;
}

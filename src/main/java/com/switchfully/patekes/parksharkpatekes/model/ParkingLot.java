package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ParkingLot {
    @Id
    @SequenceGenerator(name="parkinglot_seq", sequenceName = "parkinglot_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parkinglot_seq")
    private long id;
    @ManyToOne
    @JoinColumn(name = "fk_division_id")
    private Division division;
    @Embedded
    private Name name;
    @OneToOne
    private ContactPerson contactPerson;
    @OneToOne
    private Address address;
    private final int max_capacity;
    @Enumerated(EnumType.STRING)
    private Category category;
    private final int price_per_hour;

    public ParkingLot() {
        this(0, 0);
    }

    public ParkingLot(int max_capacity, int price_per_hour) {
        this.max_capacity = max_capacity;
        this.price_per_hour = price_per_hour;
    }
}

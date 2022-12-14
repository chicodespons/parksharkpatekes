package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;
import org.hibernate.action.internal.OrphanRemovalAction;

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
    private String name;
    @ManyToOne
    private ContactPerson contactPerson;
    @OneToOne
    private Address address;
    private final int max_capacity;
    @Enumerated(EnumType.STRING)
    private Category category;
    private final int price_per_hour;
    private int present_capacity = 0;

    public ParkingLot() {
        this(0, 0);
    }

    public ParkingLot(int max_capacity, int price_per_hour) {
        this.max_capacity = max_capacity;
        this.price_per_hour = price_per_hour;
    }

    public ParkingLot(Division division, String name, ContactPerson contactPerson, Address address, int max_capacity, Category category, int price_per_hour) {
        this.division = division;
        this.name = name;
        this.contactPerson = contactPerson;
        this.address = address;
        this.max_capacity = max_capacity;
        this.category = category;
        this.price_per_hour = price_per_hour;
    }
}

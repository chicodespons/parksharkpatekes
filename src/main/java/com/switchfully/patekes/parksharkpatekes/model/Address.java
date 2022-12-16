package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class Address {
    @Id
    @SequenceGenerator(name="address_seq", sequenceName = "address_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    private long id;
    private String streetName;

    private int number;

    @ManyToOne
    private PostalCode postalCode;

    public Address(String streetName, int number, PostalCode postalCode) {
        this.streetName = streetName;
        this.number = number;
        this.postalCode = postalCode;
    }

    public Address() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return number == address.number && Objects.equals(streetName, address.streetName) && Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetName, number, postalCode);
    }
}

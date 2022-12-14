package com.switchfully.patekes.parksharkpatekes.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class PostalCode {
    @Id
    @SequenceGenerator(name="postalcode_seq", sequenceName = "postalcode_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postalcode_seq")
    private long id;
    private int actualPostalCode;
    private String cityLabel;

    public PostalCode(int actualPostalCode, String cityLabel) {
        this.actualPostalCode = actualPostalCode;
        this.cityLabel = cityLabel;
    }

    public PostalCode() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostalCode that = (PostalCode) o;
        return actualPostalCode == that.actualPostalCode && Objects.equals(cityLabel, that.cityLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualPostalCode, cityLabel);
    }
}

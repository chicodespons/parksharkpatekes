package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Data
public class LicensePlate {
    @Id
    private String plateId;

    private String issuingCountry;

    public LicensePlate(String plateId, String issuingCountry) {
        this.plateId = plateId;
        this.issuingCountry = issuingCountry;
    }

    public LicensePlate() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicensePlate that)) return false;
        return Objects.equals(plateId, that.plateId) && Objects.equals(issuingCountry, that.issuingCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateId, issuingCountry);
    }
}

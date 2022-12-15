package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class LicensePlate {
    @Id
    private String plateId;
    private String issuingCountry;
    //.getDisplayCountry()


    public LicensePlate(String plateId, String issuingCountry) {
        this.plateId = plateId;
        this.issuingCountry = issuingCountry;
    }

    public LicensePlate() {
    }
}

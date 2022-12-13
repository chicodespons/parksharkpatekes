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
}

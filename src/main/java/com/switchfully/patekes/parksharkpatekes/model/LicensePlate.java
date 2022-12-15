package com.switchfully.patekes.parksharkpatekes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicensePlate {
    @Id
    private String plateId;
    private String issuingCountry;
}

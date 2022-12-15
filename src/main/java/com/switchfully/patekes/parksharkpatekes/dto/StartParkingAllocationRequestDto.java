package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
public class StartParkingAllocationRequestDto {
    @NotNull
    private LicensePlate licensePlate;
    @Min(1)
    private Long parkingLotId;
}

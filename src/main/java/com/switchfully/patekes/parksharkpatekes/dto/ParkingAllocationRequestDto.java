package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
public class ParkingAllocationRequestDto {
    @Min(1)
    private Long memberId;
    @NotNull
    private LicensePlate licensePlate;
    @Min(1)
    private Long parkingLotId;
}

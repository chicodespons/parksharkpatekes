package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
public class EndParkingAllocationRequestDto {
    @Min(1)
    private Long memberId;
    @Min(1)
    private Long allocationId;
}

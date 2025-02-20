package com.switchfully.patekes.parksharkpatekes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EndParkingAllocationRequestDto {
    @Min(1)
    private Long allocationId;
}

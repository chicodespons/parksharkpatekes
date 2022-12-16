package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingAllocationOverviewDto {
    private MemberParkingAllocationOverviewDto memberParkingAllocationOverviewDto;
    private Long allocationId;
    private boolean active;
    private LocalDateTime start_time;
    private LocalDateTime stop_time;
    private LicensePlate licensePlate;
}

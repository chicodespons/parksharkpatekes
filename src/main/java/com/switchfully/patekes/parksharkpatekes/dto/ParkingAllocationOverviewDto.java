package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAllocationOverviewDto {
    private MemberParkingAllocationOverviewDto memberParkingAllocationOverviewDto;
    private Long allocationId;
    private boolean active;
    private LocalDateTime start_time;
    private LocalDateTime stop_time;
    private LicensePlate licensePlate;
}

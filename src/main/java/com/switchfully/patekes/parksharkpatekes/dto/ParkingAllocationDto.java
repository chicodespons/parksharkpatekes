package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.Member;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAllocationDto {
    private Member member;
    private ParkingLot parkingLot;
    private boolean active;
    private LocalDateTime start_time;
    private LocalDateTime stop_time;
    private LicensePlate licensePlate;
}

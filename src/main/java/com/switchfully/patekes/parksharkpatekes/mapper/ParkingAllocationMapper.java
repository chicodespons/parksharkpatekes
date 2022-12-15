package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.MemberParkingAllocationOverviewDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationOverviewDto;
import com.switchfully.patekes.parksharkpatekes.model.ParkingAllocation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParkingAllocationMapper {
    public ParkingAllocationDto toDto(ParkingAllocation parkingAllocation) {
        return new ParkingAllocationDto(
                parkingAllocation.getId(),
                parkingAllocation.getMember(),
                parkingAllocation.getParkingLot(),
                parkingAllocation.isActive(),
                parkingAllocation.getStartTime(),
                parkingAllocation.getStopTime(),
                parkingAllocation.getLicensePlate()
        );
    }

    public List<ParkingAllocationDto> toDto(List<ParkingAllocation> parkingAllocationList) {
        return parkingAllocationList.stream()
                .map(parkingAllocation -> toDto(parkingAllocation))
                .toList();
    }

    public ParkingAllocationOverviewDto toOverviewDto(ParkingAllocation parkingAllocation) {
        return new ParkingAllocationOverviewDto(
                new MemberParkingAllocationOverviewDto(
                        parkingAllocation.getMember().getId(),
                        parkingAllocation.getMember().getName(),
                        parkingAllocation.getMember().getLicensePlate(),
                        parkingAllocation.getMember().getMembershipLvl()
                        ),
                parkingAllocation.getId(),
                parkingAllocation.isActive(),
                parkingAllocation.getStartTime(),
                parkingAllocation.getStopTime(),
                parkingAllocation.getLicensePlate()
        );
    }

    public List<ParkingAllocationOverviewDto> toOverviewDto(List<ParkingAllocation> parkingAllocationList) {
        return parkingAllocationList.stream()
                .map(parkingAllocation -> toOverviewDto(parkingAllocation))
                .toList();
    }
}

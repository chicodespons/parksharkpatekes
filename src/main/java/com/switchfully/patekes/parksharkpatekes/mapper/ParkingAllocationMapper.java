package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
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
}

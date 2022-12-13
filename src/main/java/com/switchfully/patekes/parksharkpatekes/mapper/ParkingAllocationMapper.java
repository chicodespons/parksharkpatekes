package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.model.ParkingAllocation;
import org.springframework.stereotype.Component;

@Component
public class ParkingAllocationMapper {
    public ParkingAllocationDto toDto(ParkingAllocation parkingAllocation) {
        return new ParkingAllocationDto(
                parkingAllocation.getMember(),
                parkingAllocation.getParkingLot(),
                parkingAllocation.isActive(),
                parkingAllocation.getStart_time(),
                parkingAllocation.getStop_time(),
                parkingAllocation.getLicensePlate()
        );
    }

    public ParkingAllocation toModel(ParkingAllocationDto parkingAllocationDto) {
        return new ParkingAllocation(
                parkingAllocationDto.getMember(),
                parkingAllocationDto.getParkingLot(),
                parkingAllocationDto.isActive(),
                parkingAllocationDto.getStart_time(),
                parkingAllocationDto.getStop_time(),
                parkingAllocationDto.getLicensePlate()
        );
    }
}

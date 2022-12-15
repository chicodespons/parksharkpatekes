package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParkingLotMapper {

    public ParkingLotDTO toDTO(ParkingLot parkingLot) {
        return new ParkingLotDTO(parkingLot.getId(), parkingLot.getDivision(), parkingLot.getName(), parkingLot.getContactPerson(),
                parkingLot.getAddress(), parkingLot.getMax_capacity(), parkingLot.getCategory(), parkingLot.getPrice_per_hour());
    }

    public List<ParkingLotDTO> toDTO(List<ParkingLot> allDivisions) {
        return allDivisions.stream()
                .map(this::toDTO)
                .toList();
    }
}

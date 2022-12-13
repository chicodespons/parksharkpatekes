package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingLotMapper;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ParkingLotService {
    private ParkingLotRepository parkingLotRepository;
    private ParkingLotMapper parkingLotMapper;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingLotMapper parkingLotMapper) {
        this.parkingLotRepository = parkingLotRepository;
        this. parkingLotMapper = parkingLotMapper;
    }

    @Transactional
    public ParkingLotDTO addParkingLot(CreateParkingLotDTO createParkingLotDTO) {

        ParkingLot parkingLot = new ParkingLot(createParkingLotDTO.division(), createParkingLotDTO.name(), createParkingLotDTO.contactPerson(),
                createParkingLotDTO.address(), createParkingLotDTO.max_capacity(), createParkingLotDTO.category(), createParkingLotDTO.price_per_hour());
        return parkingLotMapper.toDTO(parkingLotRepository.save(parkingLot));
    }
}

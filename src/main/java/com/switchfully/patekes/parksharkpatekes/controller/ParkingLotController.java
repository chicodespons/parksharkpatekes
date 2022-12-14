package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import com.switchfully.patekes.parksharkpatekes.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "parkinglot")
public class ParkingLotController {
    private ParkingLotService parkingLotService;
    public ParkingLotController(ParkingLotService parkingLotService){this.parkingLotService = parkingLotService;}

    @PostMapping(path = "add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ParkingLotDTO addParkingLot(@RequestBody CreateParkingLotDTO createParkingLotDTO) {
        return parkingLotService.addParkingLot(createParkingLotDTO);
    }
}

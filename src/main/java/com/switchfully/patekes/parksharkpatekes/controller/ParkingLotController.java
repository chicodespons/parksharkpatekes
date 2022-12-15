package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.service.ParkingLotService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "parkinglot")
public class ParkingLotController {
    private ParkingLotService parkingLotService;
    public ParkingLotController(ParkingLotService parkingLotService){this.parkingLotService = parkingLotService;}
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ParkingLotDTO addParkingLot(@RequestBody CreateParkingLotDTO createParkingLotDTO) {
        return parkingLotService.addParkingLot(createParkingLotDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParkingLotDTO> getAllParkingLots(){
        return parkingLotService.getAllParkingLots();
    }
}

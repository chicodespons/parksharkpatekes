package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import com.switchfully.patekes.parksharkpatekes.service.ParkingAllocationService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingAllocationService parkingAllocationService;

    public ParkingController(ParkingAllocationService parkingAllocationService) {
        this.parkingAllocationService = parkingAllocationService;
    }

    @PostMapping(path = "parking_allocation", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    private ParkingAllocationDto allocateParkingSpot(@RequestBody ParkingAllocationRequestDto parkingAllocationRequestDto) throws ParkingLotException, MemberException {
        return parkingAllocationService.allocateParkingSpot(parkingAllocationRequestDto);
    }
}

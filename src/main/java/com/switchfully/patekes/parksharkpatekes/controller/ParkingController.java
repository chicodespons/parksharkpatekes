package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.EndParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.StartParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.LicencePlateException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingAllocationException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import com.switchfully.patekes.parksharkpatekes.service.ParkingAllocationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingAllocationService parkingAllocationService;

    public ParkingController(ParkingAllocationService parkingAllocationService) {
        this.parkingAllocationService = parkingAllocationService;
    }

    @GetMapping("allocation")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<ParkingAllocationDto> getAllParkingAllocations() {
        return parkingAllocationService.getAllAllocations();
    }

    @PostMapping(path = "allocation/start", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ParkingAllocationDto allocateParkingSpot(@RequestBody StartParkingAllocationRequestDto startParkingAllocationRequestDto,
                                                    BindingResult bindingResult) throws ParkingLotException, MemberException, ParkingAllocationException, LicencePlateException {
        if (bindingResult.hasErrors()) {
            throw new ParkingAllocationException("Some fields were not filled in properly.");
        }
        return parkingAllocationService.allocateParkingSpot(startParkingAllocationRequestDto);
    }

    @PutMapping(path = "allocation/stop", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ParkingAllocationDto allocateParkingSpot(@RequestBody EndParkingAllocationRequestDto endParkingAllocationRequestDto,
                                                    BindingResult bindingResult) throws MemberException, ParkingAllocationException {
        if (bindingResult.hasErrors()) {
            throw new ParkingAllocationException("Some fields were not filled in properly.");
        }
        return parkingAllocationService.deAllocateParkingSpot(endParkingAllocationRequestDto);
    }
}

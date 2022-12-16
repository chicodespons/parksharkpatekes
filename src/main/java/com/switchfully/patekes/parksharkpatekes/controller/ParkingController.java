package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.EndParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationOverviewDto;
import com.switchfully.patekes.parksharkpatekes.dto.StartParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.LicencePlateException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingAllocationException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import com.switchfully.patekes.parksharkpatekes.service.ParkingAllocationService;
import net.minidev.json.parser.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingAllocationService parkingAllocationService;

    public ParkingController(ParkingAllocationService parkingAllocationService) {
        this.parkingAllocationService = parkingAllocationService;
    }

    @GetMapping(path = "allocation", produces = "application/json")
    @ResponseStatus(OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<ParkingAllocationOverviewDto> getAllParkingAllocations(@RequestParam(required = false, defaultValue = "100") int limit,
                                                                       @RequestParam(required = false) Optional<Boolean> isActive,
                                                                       @RequestParam(required = false, defaultValue = "true") boolean ascending) {
        return parkingAllocationService.getAllAllocations(limit, isActive, ascending);
    }

    @PostMapping(path = "allocation", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ParkingAllocationDto allocateParkingSpot(@RequestHeader String authorization, @RequestBody StartParkingAllocationRequestDto startParkingAllocationRequestDto,
                                                    BindingResult bindingResult) throws ParkingLotException, MemberException, ParkingAllocationException, LicencePlateException, ParseException {
        if (bindingResult.hasErrors()) {
            throw new ParkingAllocationException("Some fields were not filled in properly.");
        }
        return parkingAllocationService.allocateParkingSpot(authorization, startParkingAllocationRequestDto);
    }

    @PutMapping(path = "allocation", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ParkingAllocationDto deAllocateParkingSpot(@RequestHeader String authorization, @RequestBody EndParkingAllocationRequestDto endParkingAllocationRequestDto,
                                                    BindingResult bindingResult) throws MemberException, ParkingAllocationException, ParseException {
        if (bindingResult.hasErrors()) {
            throw new ParkingAllocationException("Some fields were not filled in properly.");
        }
        return parkingAllocationService.deAllocateParkingSpot(authorization, endParkingAllocationRequestDto);
    }
}

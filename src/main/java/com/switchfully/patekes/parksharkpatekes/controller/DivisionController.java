package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.CreateDivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.NoDivisionFoundException;
import com.switchfully.patekes.parksharkpatekes.service.DivisionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;



@RestController
@RequestMapping("divisions")
public class DivisionController {

    private final DivisionService divisionService;

    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    public DivisionDto createDivision(@Valid @RequestBody CreateDivisionDto createDivisionDto){
        return divisionService.createDivision(createDivisionDto);
    }

     @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = "application/json")
    public List<DivisionDto> getAllDivisions() {

        return divisionService.getAllDivisions();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(params = "id", produces = "application/json")
    public DivisionDto getDivisionById(@RequestParam Long id) throws NoDivisionFoundException {
        return divisionService.getDivisionById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(params = "name", produces = "application/json")
    public List<DivisionDto> getDivisionByName(@RequestParam String name) {
        return divisionService.getDivisionByName(name);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path= "{parentId}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    public DivisionDto createSubdivision(@PathVariable Long parentId, @Valid @RequestBody CreateDivisionDto createDivisionDto) throws NoDivisionFoundException {
        return divisionService.createSubdivision(parentId, createDivisionDto);
    }
}

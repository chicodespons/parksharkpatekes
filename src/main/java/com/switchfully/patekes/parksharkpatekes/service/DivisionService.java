package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.CreateDivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.NoDivisionFoundException;
import com.switchfully.patekes.parksharkpatekes.mapper.DivisionMapper;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DivisionService {
    private final DivisionRepository divisionRepository;
    private final DivisionMapper divisionMapper;

    public DivisionService(DivisionRepository divisionRepository, DivisionMapper divisionMapper) {
        this.divisionRepository = divisionRepository;
        this.divisionMapper = divisionMapper;
    }

    public DivisionDto createDivision(CreateDivisionDto createDivisionDto) {
        Division division = new Division(createDivisionDto.getName(), createDivisionDto.getOriginalName(), createDivisionDto.getDirector());
        divisionRepository.save(division);
        return divisionMapper.toDto(division);
    }

    public List<DivisionDto> getAllDivisions() {
        return divisionMapper.toDto(divisionRepository.findAll());
    }

    public DivisionDto createSubdivision(long parentId, CreateDivisionDto createDivisionDto) throws NoDivisionFoundException {
        Division parentDivision = divisionRepository.findById(parentId).orElseThrow(() -> new NoDivisionFoundException("No division found with the id: " + parentId + " ,the subdivision creation is cancelled."));
        DivisionDto divisionDto = createDivision(createDivisionDto);
        parentDivision.addSubdivision(divisionRepository.findById(divisionDto.id()).orElseThrow(() -> new NoDivisionFoundException("No division found with the id: " + parentId + " ,the subdivision creation is cancelled.")));
        return divisionDto;
    }

    public DivisionDto getDivisionById(Long id) throws NoDivisionFoundException {
        return divisionMapper.toDto(divisionRepository.findById(id).orElseThrow(() -> new NoDivisionFoundException("No division found with the id: " + id)));
    }

    public List<DivisionDto> getDivisionByName(String name) {
        return divisionMapper.toDto(divisionRepository.findDivisionByName(name));
    }
}

package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.CreateDivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.mapper.DivisionMapper;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Division division = new Division(createDivisionDto.getName(),createDivisionDto.getOriginalName(),createDivisionDto.getDirector());
        divisionRepository.save(division);
        return divisionMapper.toDto(division);
    }
}

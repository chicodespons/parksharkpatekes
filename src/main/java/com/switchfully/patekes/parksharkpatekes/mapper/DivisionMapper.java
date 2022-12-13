package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import org.springframework.stereotype.Component;

@Component
public class DivisionMapper {
    public DivisionDto toDto(Division division) {
        return new DivisionDto(division.getName(), division.getOriginalName(), division.getDirector());
    }
}

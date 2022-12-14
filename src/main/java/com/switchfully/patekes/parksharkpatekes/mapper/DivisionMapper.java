package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DivisionMapper {
    public DivisionDto toDto(Division division) {
        return new DivisionDto(division.getId(), division.getName(), division.getOriginalName(), division.getDirector(), toDto(division.getSubdivisions()));
    }


    public List<DivisionDto> toDto(List<Division> allDivisions) {
        return allDivisions.stream()
                .map(this::toDto)
                .toList();
    }
}

package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Division;

import java.util.List;

public record DivisionDto(Long id,
                          String name,
                          String originalName,
                          String director,
                          List<DivisionDto> subdivisions) {
}

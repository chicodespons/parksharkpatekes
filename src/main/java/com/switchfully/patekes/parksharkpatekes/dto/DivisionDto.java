package com.switchfully.patekes.parksharkpatekes.dto;

import java.util.List;

public record DivisionDto(Long id,
                          String name,
                          String originalName,
                          String director,
                          List<DivisionDto> subdivisions) {
}

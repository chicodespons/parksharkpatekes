package com.switchfully.patekes.parksharkpatekes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateDivisionDto {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String originalName;
    @NotNull
    @NotEmpty
    private String director;

}

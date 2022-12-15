package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateParkingLotDTO(@NotEmpty long divisionID,
                                  @NotNull @NotEmpty String name,
                                  @NotNull @NotEmpty ContactPerson contactPerson,
                                  @NotNull @NotEmpty Address address,
                                  @NotNull @NotEmpty int max_capacity,
                                  @NotNull @NotEmpty Category category,
                                  @NotNull @NotEmpty int price_per_hour) {
}

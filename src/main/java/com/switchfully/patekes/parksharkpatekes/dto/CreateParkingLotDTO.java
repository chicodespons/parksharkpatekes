package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.*;

public record CreateParkingLotDTO(long divisionID, String name, ContactPerson contactPerson, Address address
                                , int max_capacity, Category category, int price_per_hour) {
}

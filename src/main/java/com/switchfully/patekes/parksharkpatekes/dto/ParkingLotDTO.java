package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.*;

public record ParkingLotDTO(long id, Division division, String name, ContactPerson contactPerson, Address address
                            , int max_capacity, Category category, int price_per_hour) {
}

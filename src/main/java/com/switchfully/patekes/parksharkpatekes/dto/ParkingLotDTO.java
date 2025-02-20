package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import com.switchfully.patekes.parksharkpatekes.model.Category;
import com.switchfully.patekes.parksharkpatekes.model.ContactPerson;
import com.switchfully.patekes.parksharkpatekes.model.Division;

import java.util.Objects;

public record ParkingLotDTO(long id,
                            Division division,
                            String name,
                            ContactPerson contactPerson,
                            Address address,
                            int max_capacity,
                            Category category,
                            int price_per_hour) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLotDTO that = (ParkingLotDTO) o;
        return max_capacity == that.max_capacity && price_per_hour == that.price_per_hour && Objects.equals(division, that.division) && Objects.equals(name, that.name) && Objects.equals(contactPerson, that.contactPerson) && Objects.equals(address, that.address) && category == that.category;
    }
}

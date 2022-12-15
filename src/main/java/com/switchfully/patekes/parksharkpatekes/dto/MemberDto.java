package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.MembershipLvl;
import com.switchfully.patekes.parksharkpatekes.model.Name;

import java.time.LocalDateTime;
import java.util.Objects;

public record MemberDto(long id,
                        Name name,
                        String phoneNumber,
                        String Email,
                        MembershipLvl membershipLvl,
                        LicensePlate licensePlate,
                        LocalDateTime registrationDate,
                        Address address) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(name, memberDto.name) && Objects.equals(phoneNumber, memberDto.phoneNumber) && Objects.equals(Email, memberDto.Email) && membershipLvl == memberDto.membershipLvl && Objects.equals(licensePlate, memberDto.licensePlate) && Objects.equals(address, memberDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, Email, membershipLvl, licensePlate, address);
    }
}

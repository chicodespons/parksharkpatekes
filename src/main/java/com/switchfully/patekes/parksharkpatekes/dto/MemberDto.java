package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.MembershipLvl;
import com.switchfully.patekes.parksharkpatekes.model.Name;

import java.time.LocalDateTime;

public record MemberDto(long id,
                        Name name,
                        String phoneNumber,
                        String Email,
                        MembershipLvl membershipLvl,
                        LicensePlate licensePlate,
                        LocalDateTime registrationDate,
                        Address address) {
}

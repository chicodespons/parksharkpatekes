package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.MembershipLvl;
import com.switchfully.patekes.parksharkpatekes.model.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberParkingAllocationOverviewDto {
    private Long memberId;
    @Embedded
    private Name name;
    private LicensePlate ownersPlate;
    private MembershipLvl membershipLvl;
}

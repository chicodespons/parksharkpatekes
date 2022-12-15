package com.switchfully.patekes.parksharkpatekes.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MembershipLvl {
    BRONZE(0, 0.0, 4),
    SILVER(10, 0.2, 6),
    GOLD(40, 0.3, 24);

    private final int monthlyCost;
    private final double parkingSpotAllocationReduction;
    private final int maxAllocationTime;


}

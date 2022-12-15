package com.switchfully.patekes.parksharkpatekes.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record UpdateMembershipLevelDto (@NotNull @NotEmpty String membershipLvl){
}

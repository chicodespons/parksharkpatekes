package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.Name;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMemberDto {

    private String username;
    private String email;
    private String password;
    private Name name;
    private String phonenumber;
    private LicensePlate licensePlate;
    private Address address;

}

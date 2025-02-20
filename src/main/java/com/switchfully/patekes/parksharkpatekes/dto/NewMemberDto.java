package com.switchfully.patekes.parksharkpatekes.dto;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.Name;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewMemberDto {
    @NotNull
    @NotEmpty
    private String username;
    @Email
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @Embedded
    private Name name;
    @NotNull
    @NotEmpty
    private String phonenumber;
    @NotNull
    private LicensePlate licensePlate;

    @NotNull
    private Address address;
    private String membershiplevel;

    public NewMemberDto(String username, String email, String password, Name name, String phonenumber, LicensePlate licensePlate, Address address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
        this.licensePlate = licensePlate;
        this.address = address;
    }

    public NewMemberDto(String username, String email, String password, Name name, String phonenumber, LicensePlate licensePlate, Address address, String membershiplevel) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
        this.licensePlate = licensePlate;
        this.address = address;
        this.membershiplevel = membershiplevel;
    }

    public NewMemberDto() {
    }
}

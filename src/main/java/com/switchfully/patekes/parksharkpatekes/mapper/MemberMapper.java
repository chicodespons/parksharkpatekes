package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import com.switchfully.patekes.parksharkpatekes.model.Member;
import com.switchfully.patekes.parksharkpatekes.model.MembershipLvl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberMapper {

    public Member CreateMemberfromMemberDto(NewMemberDto newMemberDto) {
        return new Member(newMemberDto.getName(),
                newMemberDto.getPhonenumber(),
                newMemberDto.getEmail(),
                newMemberDto.getPassword(),
                MembershipLvl.BRONZE,
                newMemberDto.getLicensePlate(),
                newMemberDto.getAddress());
    }

}

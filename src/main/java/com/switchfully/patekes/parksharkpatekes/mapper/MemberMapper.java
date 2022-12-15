package com.switchfully.patekes.parksharkpatekes.mapper;

import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import com.switchfully.patekes.parksharkpatekes.model.Member;
import com.switchfully.patekes.parksharkpatekes.model.MembershipLvl;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
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

    public MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getPhoneNumber(), member.getEmail(),
                                member.getMembershipLvl(), member.getLicensePlate(), member.getRegistrationDate(), member.getAddress());
    }

    public List<MemberDto> toDto(List<Member> allMembers) {
        return allMembers.stream()
                .map(this::toDto)
                .toList();
    }

}

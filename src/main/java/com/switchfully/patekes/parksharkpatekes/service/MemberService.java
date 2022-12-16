package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.UpdateMembershipLevelDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.mapper.MemberMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.AddressRepository;
import com.switchfully.patekes.parksharkpatekes.repository.LicensePlateRepository;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
import com.switchfully.patekes.parksharkpatekes.repository.PostalCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final LicensePlateRepository licensePlateRepository;
    private final MemberMapper memberMapper;
    private final PostalCodeRepository postalCodeRepository;
    private final AddressRepository addressRepository;

    public MemberService(MemberRepository memberRepository, LicensePlateRepository licensePlateRepository, MemberMapper memberMapper,
                         PostalCodeRepository postalCodeRepository, AddressRepository addressRepository) {
        this.memberRepository = memberRepository;
        this.licensePlateRepository = licensePlateRepository;
        this.memberMapper = memberMapper;
        this.postalCodeRepository = postalCodeRepository;
        this.addressRepository = addressRepository;
    }


    public MemberDto addUser(NewMemberDto newMemberDto) throws MemberException {
        // alle elementen die gesaved moeten worden in volgorde voor dat de eigenlijke memeber kan gesaved worden
        Member member = memberMapper.CreateMemberfromMemberDto(newMemberDto);
        MembershipLvl membershipLvl = checkMemberShipLevel(newMemberDto.getMembershiplevel());
        member.setMembershipLvl(membershipLvl);
        LicensePlate licensePlate = member.getLicensePlate();
        // save methodes
        licensePlateRepository.save(licensePlate);
        Address address = checkAddress(member.getAddress());
        member.setAddress(address);
        return memberMapper.toDto(memberRepository.save(member));
    }

    public List<MemberDto> getAllMembers() {
        return memberMapper.toDto(memberRepository.findAll());
    }

    public List<Member> getAllMembersAsMembers(){
        return memberRepository.findAll();
    }
    private PostalCode checkPostalCode(PostalCode postalCode) {
        PostalCode postalCodeRepo = postalCodeRepository.findByActualPostalCodeAndCityLabel(postalCode.getActualPostalCode(),postalCode.getCityLabel());
        if (postalCodeRepo != null) {
            return postalCodeRepo;
        }
        return postalCodeRepository.save(postalCode);
    }

    private Address checkAddress(Address address) {
        PostalCode tempPC = checkPostalCode(address.getPostalCode());
        address.setPostalCode(tempPC);
        return addressRepository.save(address);
    }

    private Member checkMember(Long id, String email) throws MemberException {
        Optional<Member> memberFromDb = memberRepository.findById(id);
        if (memberFromDb.isEmpty()) {
            throw new MemberException("Could not find specified member.");
        }
       if (!Objects.equals(memberFromDb.get().getEmail(), email)) {
            throw new MemberException("Not authorized, tried to update wrong account");
        }
        else return memberFromDb.get();
    }

    private MembershipLvl checkMemberShipLevel(String membershiplevel) throws MemberException {
        if (membershiplevel == null || membershiplevel.isEmpty() || membershiplevel.equalsIgnoreCase("bronze")) {
            return MembershipLvl.BRONZE;
        }
        else if (membershiplevel.equalsIgnoreCase("silver")) {
            return MembershipLvl.SILVER;
        }
        else if (membershiplevel.equalsIgnoreCase("gold")) {
            return MembershipLvl.GOLD;
        }
        else {
            throw new MemberException("Wrong input given: please select a valid Membershiplevel (bronze, silver or gold).");
        }
    }


    public MemberDto updateMembershipLevel(UpdateMembershipLevelDto updateMembershipLevelDto, Long id, String email) throws MemberException {
        Member memberToUpdate = checkMember(id, email);
        memberToUpdate.setMembershipLvl(checkMemberShipLevel(updateMembershipLevelDto.membershiplevel()));
        return memberMapper.toDto(memberRepository.save(memberToUpdate));

    }
}

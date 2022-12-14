package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.mapper.MemberMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.AddressRepository;
import com.switchfully.patekes.parksharkpatekes.repository.LicensePlateRepository;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
import com.switchfully.patekes.parksharkpatekes.repository.PostalCodeRepository;
import org.springframework.stereotype.Service;

import static org.apache.coyote.http11.Constants.a;

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


    public void addUser(NewMemberDto newMemberDto) {
        // alle elementen die gesaved moeten worden in volgorde voor dat de eigenlijke memeber kan gesaved worden
        Member member =  memberMapper.CreateMemberfromMemberDto(newMemberDto);
        LicensePlate licensePlate = member.getLicensePlate();
        // save methodes
        licensePlateRepository.save(licensePlate);
        Address address = checkAddress(member.getAddress());
        member.setAddress(address);
        memberRepository.save(member);
    }


    private PostalCode checkPostalCode(PostalCode postalCode) {
        PostalCode postalCodeRepo = postalCodeRepository.findByCityLabel(postalCode.getCityLabel());
        if (postalCodeRepo != null && postalCodeRepo.equals(postalCode)) {
            return postalCodeRepo;
        }
        return postalCodeRepository.save(postalCode);
    }

    private Address checkAddress(Address address) {
        PostalCode tempPC = checkPostalCode(address.getPostalCode());
        address.setPostalCode(tempPC);
        return addressRepository.save(address);
    }
}

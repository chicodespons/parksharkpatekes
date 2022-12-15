package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.EndParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationOverviewDto;
import com.switchfully.patekes.parksharkpatekes.dto.StartParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.LicencePlateException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingAllocationException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingAllocationMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.LicensePlateRepository;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingAllocationRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingLotRepository;
import com.switchfully.patekes.parksharkpatekes.security.TokenDecoder;
import net.minidev.json.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingAllocationService {
    private final ParkingAllocationMapper parkingAllocationMapper;
    private final ParkingAllocationRepository parkingAllocationRepository;
    private final MemberRepository memberRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final LicensePlateRepository licensePlateRepository;
    private final EntityManager entityManager;

    public ParkingAllocationService(ParkingAllocationMapper parkingAllocationMapper, ParkingAllocationRepository parkingAllocationRepository, MemberRepository memberRepository, ParkingLotRepository parkingLotRepository, LicensePlateRepository licensePlateRepository, EntityManager entityManager) {
        this.parkingAllocationMapper = parkingAllocationMapper;
        this.parkingAllocationRepository = parkingAllocationRepository;
        this.memberRepository = memberRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.licensePlateRepository = licensePlateRepository;
        this.entityManager = entityManager;
    }


    public ParkingAllocationDto allocateParkingSpot(String authorization, StartParkingAllocationRequestDto parkingAllocationRequestDto) throws MemberException, ParkingLotException, LicencePlateException, ParseException {
        Member member = validateMember(authorization);
        ParkingLot parkingLot = validateParkingLot(parkingAllocationRequestDto.getParkingLotId());
        LicensePlate licensePlate = validateLicensePlate(parkingAllocationRequestDto.getLicensePlate(), member.getLicensePlate(), member.getMembershipLvl().equals(MembershipLvl.GOLD));

        ParkingAllocation newParkingAllocation = new ParkingAllocation(member, parkingLot, licensePlate);
        parkingAllocationRepository.save(newParkingAllocation);

        return parkingAllocationMapper.toDto(parkingAllocationRepository.findById(newParkingAllocation.getId()).get());
    }

    private Member validateMember(String authorization) throws MemberException, ParseException {
        Optional<Member> memberFromDb = memberRepository.findMemberByEmail(TokenDecoder.tokenDecode(authorization));
        if (memberFromDb.isEmpty()) {
            throw new MemberException("Could not find member.");
        }
        return memberFromDb.get();
    }

    private ParkingLot validateParkingLot(Long parkingLotId) throws ParkingLotException {
        Optional<ParkingLot> parkingLotFromDb = parkingLotRepository.findById(parkingLotId);
        if (parkingLotFromDb.isEmpty()) {
            throw new ParkingLotException("Could not find specified parking lot.");
        } else if (parkingLotFromDb.get().getPresentCapacity() == parkingLotFromDb.get().getMax_capacity()) {
            throw new ParkingLotException("Sorry, parking lot is full!");
        }
        return parkingLotFromDb.get();
    }

    private LicensePlate validateLicensePlate(LicensePlate licensePlate, LicensePlate membersPlate, boolean hasGoldLevel) throws LicencePlateException {
        Optional<LicensePlate> licensePlateFromDb = licensePlateRepository.findById(licensePlate.getPlateId());
        if (licensePlateFromDb.isEmpty() && hasGoldLevel) {
            licensePlateRepository.save(licensePlate);
        } else if (licensePlateFromDb.isEmpty()) {
            throw new LicencePlateException("Could not find license plate.");
        } else if (!hasGoldLevel && !licensePlateFromDb.get().equals(membersPlate)) {
            throw new LicencePlateException("Only gold members can register any plate.");
        }
        return licensePlateRepository.findById(licensePlate.getPlateId()).get();
    }

    public ParkingAllocationDto deAllocateParkingSpot(String authorization, EndParkingAllocationRequestDto endParkingAllocationRequestDto) throws MemberException, ParkingAllocationException, ParseException {
        Member member = validateMember(authorization);
        ParkingAllocation parkingAllocation = validateParkingAllocation(endParkingAllocationRequestDto.getAllocationId());
        ParkingLot parkingLot = parkingLotRepository.findById(parkingAllocation.getParkingLot().getId()).get();

        if (allInfoIsCorrect(parkingAllocation, member.getId())) {
            parkingAllocation.setActive(false);
            parkingAllocation.setStopTime(LocalDateTime.now());
            parkingLot.setPresentCapacity(parkingLot.getPresentCapacity() - 1);

            parkingAllocationRepository.save(parkingAllocation);
            // parking lot also gets updated when persisting changes to allocation
        }
        return parkingAllocationMapper.toDto(parkingAllocationRepository.findById(parkingAllocation.getId()).get());
    }

    private ParkingAllocation validateParkingAllocation(Long allocationId) throws ParkingAllocationException {
        Optional<ParkingAllocation> parkingAllocationFromDb = parkingAllocationRepository.findById(allocationId);
        if (parkingAllocationFromDb.isEmpty()) {
            throw new ParkingAllocationException("Could not find the specified allocation id.");
        }
        return parkingAllocationFromDb.get();
    }

    private boolean allInfoIsCorrect(ParkingAllocation parkingAllocation, Long memberId) throws ParkingAllocationException {
        if (!parkingAllocation.isActive()) {
            throw new ParkingAllocationException("Cannot stop an allocation that has already been stopped.");
        } else if (!memberId.equals(parkingAllocation.getMember().getId())) {
            throw new ParkingAllocationException("You are not the owner of this parking allocation");
        }
        return true;
    }

    public List<ParkingAllocationOverviewDto> getAllAllocations(int limit, Optional<Boolean> isActive, boolean ascending) {
        List<ParkingAllocation> parkingAllocationList;
        Sort sortingOrder = defineSortingOrder(ascending);

        if (isActive.isEmpty()) {
            parkingAllocationList = parkingAllocationRepository.findAll(PageRequest.of(0, limit, sortingOrder)).getContent();
        } else {
            parkingAllocationList = parkingAllocationRepository.findAllByActive(PageRequest.of(0, limit, sortingOrder), isActive.get());
        }

        return parkingAllocationMapper.toOverviewDto(parkingAllocationList);
    }

    private Sort defineSortingOrder(boolean ascending) {
        if (ascending) {
            return Sort.by(Sort.Direction.ASC, "startTime");
        } else {
            return Sort.by(Sort.Direction.DESC, "startTime");
        }
    }
}

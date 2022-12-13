package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.EndParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingAllocationService {
    private final ParkingAllocationMapper parkingAllocationMapper;
    private final ParkingAllocationRepository parkingAllocationRepository;
    private final MemberRepository memberRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final LicensePlateRepository licensePlateRepository;

    public ParkingAllocationService(ParkingAllocationMapper parkingAllocationMapper, ParkingAllocationRepository parkingAllocationRepository, MemberRepository memberRepository, ParkingLotRepository parkingLotRepository, LicensePlateRepository licensePlateRepository) {
        this.parkingAllocationMapper = parkingAllocationMapper;
        this.parkingAllocationRepository = parkingAllocationRepository;
        this.memberRepository = memberRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.licensePlateRepository = licensePlateRepository;
    }


    public ParkingAllocationDto allocateParkingSpot(StartParkingAllocationRequestDto parkingAllocationRequestDto) throws MemberException, ParkingLotException, LicencePlateException {
        Member member = validateMember(parkingAllocationRequestDto.getMemberId());
        ParkingLot parkingLot = validateParkingLot(parkingAllocationRequestDto.getParkingLotId());
        LicensePlate licensePlate = validateLicensePlate(parkingAllocationRequestDto.getLicensePlate(), member.getMembershipLvl().equals(MembershipLvl.GOLD));

        ParkingAllocation newParkingAllocation = new ParkingAllocation(member, parkingLot, licensePlate);
        parkingAllocationRepository.save(newParkingAllocation);

        return parkingAllocationMapper.toDto(parkingAllocationRepository.findById(newParkingAllocation.getId()).get());
    }

    private Member validateMember(Long memberId) throws MemberException {
        Optional<Member> memberFromDb = memberRepository.findById(memberId);
        if (memberFromDb.isEmpty()) {
            throw new MemberException("Could not find specified member.");
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

    private LicensePlate validateLicensePlate(LicensePlate licensePlate, boolean hasGoldLevel) throws LicencePlateException {
        Optional<LicensePlate> licensePlateFromDb = licensePlateRepository.findById(licensePlate.getPlateId());
        if (licensePlateFromDb.isEmpty() && hasGoldLevel) {
            licensePlateRepository.save(licensePlate);
        } else if (licensePlateFromDb.isEmpty()) {
            throw new LicencePlateException("Could not find license plate.");
        }
        return licensePlateRepository.findById(licensePlate.getPlateId()).get();
    }

    public ParkingAllocationDto deAllocateParkingSpot(EndParkingAllocationRequestDto endParkingAllocationRequestDto) throws MemberException, ParkingAllocationException {
        Member member = validateMember(endParkingAllocationRequestDto.getMemberId());
        ParkingAllocation parkingAllocation = validateParkingAllocation(endParkingAllocationRequestDto.getAllocationId());
        ParkingLot parkingLot = parkingLotRepository.findById(parkingAllocation.getParkingLot().getId()).get();

        if (allInfoIsCorrect(parkingAllocation, endParkingAllocationRequestDto.getMemberId())) {
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

    public List<ParkingAllocationDto> getAllAllocations() {
        List<ParkingAllocation> parkingAllocationList = parkingAllocationRepository.findAllByOrderByStartTimeAsc();
        return parkingAllocationMapper.toDto(parkingAllocationList);
    }
}

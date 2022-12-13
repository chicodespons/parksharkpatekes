package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationDto;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingAllocationRequestDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingAllocationMapper;
import com.switchfully.patekes.parksharkpatekes.model.LicensePlate;
import com.switchfully.patekes.parksharkpatekes.model.Member;
import com.switchfully.patekes.parksharkpatekes.model.ParkingAllocation;
import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import com.switchfully.patekes.parksharkpatekes.repository.LicensePlateRepository;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingAllocationRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    public ParkingAllocationDto allocateParkingSpot(ParkingAllocationRequestDto parkingAllocationRequestDto) throws MemberException, ParkingLotException {
        Optional<Member> memberFromDb = memberRepository.findById(parkingAllocationRequestDto.getMemberId());
        Optional<ParkingLot> parkingLotFromDb = parkingLotRepository.findById(parkingAllocationRequestDto.getParkingLotId());
        Optional<LicensePlate> licensePlateFromDb = licensePlateRepository.findById(parkingAllocationRequestDto.getLicensePlate().getPlateId());

        if (memberFromDb.isEmpty()) {
            throw new MemberException("Could not find specified member.");
        } else if (parkingLotFromDb.isEmpty()) {
            throw new ParkingLotException("Could not find specified parking lot.");
        }

        if (licensePlateFromDb.isEmpty()) {
            licensePlateRepository.save(parkingAllocationRequestDto.getLicensePlate());
        }


        Member member = memberFromDb.get();
        ParkingLot parkingLot = parkingLotFromDb.get();
        LicensePlate licensePlate = licensePlateRepository.findById(parkingAllocationRequestDto.getLicensePlate().getPlateId()).get();

        ParkingAllocation newParkingAllocation = new ParkingAllocation(
                member, parkingLot, true, LocalDateTime.now(), null, licensePlate
        );

        parkingAllocationRepository.save(newParkingAllocation);

        return parkingAllocationMapper.toDto(parkingAllocationRepository.findById(newParkingAllocation.getId()).get());
    }
}

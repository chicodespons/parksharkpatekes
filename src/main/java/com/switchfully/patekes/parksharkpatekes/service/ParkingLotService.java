package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingLotMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;
    private final DivisionRepository divisionRepository;
    private final AddressRepository addressRepository;
    private final ContactPersonRepository contactPersonRepository;

    private final PostalCodeRepository postalCodeRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingLotMapper parkingLotMapper, DivisionRepository devisionRepository, AddressRepository addressRepository, ContactPersonRepository contactPersonRepository, PostalCodeRepository postalCodeRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingLotMapper = parkingLotMapper;
        this.divisionRepository = devisionRepository;
        this.addressRepository = addressRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.postalCodeRepository = postalCodeRepository;
    }

    public List<ParkingLotDTO> getAllParkingLots() {
        return parkingLotMapper.toDTO(parkingLotRepository.findAll());
    }

    public ParkingLotDTO addParkingLot(CreateParkingLotDTO createParkingLotDTO) throws NoSuchElementException {
        Division div = divisionRepository.findById(createParkingLotDTO.divisionID()).orElseThrow(NoSuchElementException::new);
        ParkingLot parkingLot = new ParkingLot(div , createParkingLotDTO.name(), checkContactPerson(createParkingLotDTO.contactPerson()),
                checkAddress(createParkingLotDTO.address()), createParkingLotDTO.max_capacity(), createParkingLotDTO.category(), createParkingLotDTO.price_per_hour());
        return parkingLotMapper.toDTO(parkingLotRepository.save(parkingLot));
    }

    private PostalCode checkPostalCode(PostalCode postalCode) {
        PostalCode result = postalCodeRepository.findByActualPostalCodeAndCityLabel(postalCode.getActualPostalCode(),postalCode.getCityLabel());
        if (result != null && result.equals(postalCode)) {return result;}
        return postalCodeRepository.save(postalCode);
    }

    private Address checkAddress(Address address) {
        PostalCode tempPC = checkPostalCode(address.getPostalCode());
        address.setPostalCode(tempPC);
        return addressRepository.save(address);
    }

    private ContactPerson checkContactPerson(ContactPerson contactPerson) {
        ContactPerson result = contactPersonRepository.findByEmail(contactPerson.getEmail());
        if (result != null && result.equals(contactPerson)) {return result;}
        contactPerson.setAddress(checkAddress(contactPerson.getAddress()));
        return contactPersonRepository.save(contactPerson);
    }
}

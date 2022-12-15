package com.switchfully.patekes.parksharkpatekes.repository;

import com.switchfully.patekes.parksharkpatekes.model.ParkingLot;
import com.switchfully.patekes.parksharkpatekes.model.PostalCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalCodeRepository extends JpaRepository<PostalCode, Long> {

    PostalCode findByCityLabel(String cityLabel);

    PostalCode findByActualPostalCodeAndCityLabel(int postalcode, String cityLabel);




}
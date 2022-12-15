package com.switchfully.patekes.parksharkpatekes.repository;

import com.switchfully.patekes.parksharkpatekes.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}




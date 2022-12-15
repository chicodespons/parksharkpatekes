package com.switchfully.patekes.parksharkpatekes.repository;

import com.switchfully.patekes.parksharkpatekes.model.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    ContactPerson findByEmail(String email);

}
package com.switchfully.patekes.parksharkpatekes.repository;

import com.switchfully.patekes.parksharkpatekes.model.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {

    List<Division> findDivisionByName(String name);
}

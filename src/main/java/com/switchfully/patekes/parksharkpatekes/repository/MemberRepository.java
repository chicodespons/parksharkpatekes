package com.switchfully.patekes.parksharkpatekes.repository;

import com.switchfully.patekes.parksharkpatekes.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberByEmail(String email);
}

package com.upperlink.ulidappportal.repository;

import com.upperlink.ulidappportal.domain.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findByAin(String ain);
    List<Applicant> findByStatus(boolean status);
}

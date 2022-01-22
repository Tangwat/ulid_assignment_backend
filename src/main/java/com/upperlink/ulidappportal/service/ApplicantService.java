package com.upperlink.ulidappportal.service;

import com.upperlink.ulidappportal.domain.Applicant;
import com.upperlink.ulidappportal.dto.ApplicantDto;
import com.upperlink.ulidappportal.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {
    Applicant createApplicant(ApplicantDto applicantDto) throws Exception;
    Optional <Applicant> findByAin(String ain);
    List <Applicant> getApplicant();
    List <Applicant> findByStatus(boolean status);
    Optional <Applicant> findById(Long applicantId);
    void disableApplicant(Long applicantId) throws NotFoundException;
    long getCountOfEntities();
    Applicant deleteApplicantById(Long applicantId);
}

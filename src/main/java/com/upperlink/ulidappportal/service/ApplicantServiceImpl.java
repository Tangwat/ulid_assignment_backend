package com.upperlink.ulidappportal.service;

import com.upperlink.ulidappportal.domain.Applicant;
import com.upperlink.ulidappportal.dto.ApplicantDto;
import com.upperlink.ulidappportal.exception.AlreadyExistException;
import com.upperlink.ulidappportal.exception.NotFoundException;
import com.upperlink.ulidappportal.repository.ApplicantRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @Autowired
    ApplicantRepository applicantRepository;

    @Override
    public Applicant createApplicant(ApplicantDto applicantDto) throws AlreadyExistException {
        Optional <Applicant> applicant = applicantRepository.findByAin(applicantDto.getAin());
        if(applicantRepository.count() == 4){
            throw new IllegalStateException("Application closed");
        }
        if (!applicant.isPresent()){
            Applicant applicant1 = new Applicant(applicantDto.getAin(), applicantDto.getFirstName(),
                    applicantDto.getSurName(), applicantDto.getPhoneNumber(), applicantDto.getEmail(),
                    applicantDto.getCoverLetter(), applicantDto.isStatus(), applicantDto.getImage(),
                    applicantDto.getResume());
            return applicantRepository.save(applicant1);
        }else {
            throw new AlreadyExistException("Applicant Already Exist");
        }
    }

    @Override
    public Optional<Applicant> findByAin(String ain) {
        return applicantRepository.findByAin(ain);
    }

    @Override
    public List<Applicant> getApplicant() {
        return applicantRepository.findAll();
    }

    @Override
    public List<Applicant> findByStatus(boolean status) {
        return applicantRepository.findByStatus(status);
    }

    @Override
    public Optional<Applicant> findById(Long applicantId) {
        return applicantRepository.findById(applicantId);
    }

    @Override
    public Applicant deleteApplicantById(Long applicantId) {
        applicantRepository.deleteById(applicantId);
        return null;
    }

    @Override
    public void disableApplicant(Long applicantId) throws NotFoundException {
        Optional<Applicant> applicant = applicantRepository.findById(applicantId);
        if (!applicant.isPresent()) {
            throw new NotFoundException("applicant does not exist");
        } else {
            applicant.get().setStatus(false);
            applicantRepository.save(applicant.get());
        }
    }

    @Override
    @Transactional
    public long getCountOfEntities() {
        long count = applicantRepository.count();
        return count;
    }
}

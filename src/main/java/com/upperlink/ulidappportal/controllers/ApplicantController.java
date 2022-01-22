package com.upperlink.ulidappportal.controllers;

import com.upperlink.ulidappportal.domain.Applicant;
import com.upperlink.ulidappportal.dto.ApplicantDto;
import com.upperlink.ulidappportal.exception.NotFoundException;
import com.upperlink.ulidappportal.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApplicantController {

    private ApplicantService applicantService;


    @Autowired
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    //create applicant
    @PostMapping("/applicants")
    public Applicant createApplicant(@Valid @RequestBody ApplicantDto applicantDto) throws Exception {
        Applicant applicant;
            applicant =applicantService.createApplicant(applicantDto);
            return applicant;

    }

    // view applicant
    @GetMapping("/applicants")
    public ResponseEntity<?>getApplicants(@RequestParam(required = false) String ain) {
        if (ain != null && !ain.isEmpty()) {
            Optional<Applicant> applicant = applicantService.findByAin(ain);
            return ResponseEntity.ok(applicant);
        } else {
            List<Applicant> applicants = applicantService.getApplicant();
            return ResponseEntity.ok(applicants);
        }
    }

    // view applicants by id
    @GetMapping("/applicants/{applicant-id}")
    public ResponseEntity<?>getApplicantById(@Valid @PathVariable("applicant-id") Long applicantId){
        Optional<Applicant> applicants = applicantService.findById(applicantId);
        return ResponseEntity.ok(applicants);
    }

    // view vehicle by status
    @GetMapping("/applicants/status")
    public ResponseEntity<?> getApplicantByStatus(@RequestParam String applicantStatus){
        List <Applicant> applicants = applicantService.findByStatus(applicantStatus.toLowerCase().equals("true"));
        return ResponseEntity.ok(applicants);
    }

    // Delete applicant
    @DeleteMapping("/applicants/{applicant-id}")
    public String deleteApplicantById(@PathVariable("applicant-id") Long applicantId) {
        if (!(applicantId == null)){
            applicantService.deleteApplicantById(applicantId);
            return "Deleted Applicant id- " + applicantId;
        }
        throw new RuntimeException("Applicant id not found - " + applicantId);
    }

    // Disable applicant
    @DeleteMapping("/applicant/{applicant-id}")
    public String disableApplicant(@Valid @PathVariable("applicant-id") Long applicantId) throws NotFoundException {
        applicantService.disableApplicant(applicantId);
        return "Applicant disabled - " + applicantId;
    }

    // Get count
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public long countEntities() {
        long count = applicantService.getCountOfEntities();
        if(count <= 4){
            return count;
        }
        throw new IllegalStateException("Application closed");
    }
}


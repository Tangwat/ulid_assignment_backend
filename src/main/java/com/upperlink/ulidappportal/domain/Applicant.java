package com.upperlink.ulidappportal.domain;


import com.upperlink.ulidappportal.domain.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Entity
public class Applicant extends DateAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long applicantId;

    private String ain;

    @NotBlank
    private String firstName;

    @NotBlank
    private String surName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String email;

    @NotBlank
    @Column(name = "description", columnDefinition = "TEXT")
    private String coverLetter;

    @NotBlank
    private boolean status;

    @NotBlank
    private String resume;

    @NotBlank
    private String image;

    @OneToMany(mappedBy = "applicant")
    private List<User> user = new ArrayList<>();


    public Applicant() {

    }

    public Applicant(String ain, String firstName, String surName, String phoneNumber, String email, String coverLetter, boolean status, String image, String resume) {
        this.ain = ain;
        this.firstName = firstName;
        this.surName = surName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.coverLetter = coverLetter;
        this.status = status;
        this.image = image;
        this.resume = resume;

    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getAin() {
        return ain;
    }

    public void setAin(String ain) {
        this.ain = ain;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

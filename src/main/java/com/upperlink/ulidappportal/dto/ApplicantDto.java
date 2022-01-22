package com.upperlink.ulidappportal.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApplicantDto {

    @NotBlank
    private Long applicantId;

    @NotBlank
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
    private String coverLetter;

    @NotBlank
    private boolean status = true;

    @NotBlank
    private String resume;

    @NotBlank
    private String image;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

package com.upperlink.ulidappportal.wrappers;

import java.util.Arrays;

public class ApplicantImageWrapper {

    private Long applicantId;

    private String name;

    private String type;

    private byte[] data;

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApplicantImageWrapper{" +
                "applicantId=" + applicantId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

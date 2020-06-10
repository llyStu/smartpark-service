package com.vibe.pojo.dahua;

public class VisitorInfo extends BasicInfo {
    private String visitorName;
    private String visitorPhone;
    private String visitorSex;
    private String statusName;
    private String certificateType;
    private String certificateNumber;
    private String visitorReason;//事由

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorSex() {
        return visitorSex;
    }

    public void setVisitorSex(String visitorSex) {
        this.visitorSex = visitorSex;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getVisitorReason() {
        return visitorReason;
    }

    public void setVisitorReason(String visitorReason) {
        this.visitorReason = visitorReason;
    }


}

package com.vibe.pojo.emergency;

public class EmergencyUser {

    private Integer id;
    private String name;
    private String sex;
    private String phone;
    private String department;
    private Integer udefault;
    private Integer etid;
    private Integer egid;
    private EmergencyEventType emergencyEventType;
    private EmergencyEventGrade emergencyEventGrade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUdefault() {
        return udefault;
    }

    public void setUdefault(Integer udefault) {

        this.udefault = udefault;
    }

    public Integer getEtid() {
        return etid;
    }

    public void setEtid(Integer etid) {
        this.etid = etid;
    }

    public Integer getEgid() {
        return egid;
    }

    public void setEgid(Integer egid) {
        this.egid = egid;
    }

    public EmergencyEventType getEmergencyEventType() {
        return emergencyEventType;
    }

    public void setEmergencyEventType(EmergencyEventType emergencyEventType) {
        this.emergencyEventType = emergencyEventType;
    }

    public EmergencyEventGrade getEmergencyEventGrade() {
        return emergencyEventGrade;
    }

    public void setEmergencyEventGrade(EmergencyEventGrade emergencyEventGrade) {
        this.emergencyEventGrade = emergencyEventGrade;
    }


}

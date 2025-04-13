package com.application.blank.dto.employee;

import com.application.blank.entity.employee.Address;
import com.application.blank.entity.employee.Gender;
import com.application.blank.entity.employee.Schedule;
import com.application.blank.entity.person.Person;

import java.time.LocalDate;

public class EmployeeDTO {
    private Long employeeId;

    // PERSON
    private String names;
    private String fathersLastName;
    private String mothersLastName;
    private String phone;
    private String email;

    private Gender gender;
    private LocalDate dateOfBirth;

    private String curp;
    private String rfc;
    private String nss;

    // ADDRESS
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Long role;
    private Long schedule;

    private Long user;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long employeeId, String names, String fathersLastName, String mothersLastName, String phone, String email, Gender gender, LocalDate dateOfBirth, String curp, String rfc, String nss, String street, String city, String state, String postalCode, String country, Long role, Long schedule, Long user) {
        this.employeeId = employeeId;
        this.names = names;
        this.fathersLastName = fathersLastName;
        this.mothersLastName = mothersLastName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.curp = curp;
        this.rfc = rfc;
        this.nss = nss;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.role = role;
        this.schedule = schedule;
        this.user = user;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFathersLastName() {
        return fathersLastName;
    }

    public void setFathersLastName(String fathersLastName) {
        this.fathersLastName = fathersLastName;
    }

    public String getMothersLastName() {
        return mothersLastName;
    }

    public void setMothersLastName(String mothersLastName) {
        this.mothersLastName = mothersLastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public Long getSchedule() {
        return schedule;
    }

    public void setSchedule(Long schedule) {
        this.schedule = schedule;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}

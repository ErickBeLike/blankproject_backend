package com.application.blank.entity.person;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long personId;

    @Column
    private String names;
    @Column
    private String fathersLastName;
    @Column
    private String mothersLastName;

    @Column
    private String phone;
    @Column (unique = true)
    private String email;

    public Person() {
    }

    public Person(String names, String fathersLastName, String mothersLastName, String phone, String email) {
        this.names = names;
        this.fathersLastName = fathersLastName;
        this.mothersLastName = mothersLastName;
        this.phone = phone;
        this.email = email;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
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
}

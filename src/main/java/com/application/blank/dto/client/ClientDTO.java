package com.application.blank.dto.client;

public class ClientDTO {
    private Long clientId;

    // Flattened fields from Person
    private String names;
    private String fathersLastName;
    private String mothersLastName;
    private String phone;
    private String email;

    public ClientDTO() {
    }

    public ClientDTO(Long clientId, String names, String fathersLastName, String mothersLastName, String phone, String email) {
        this.clientId = clientId;
        this.names = names;
        this.fathersLastName = fathersLastName;
        this.mothersLastName = mothersLastName;
        this.phone = phone;
        this.email = email;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

package com.application.blank.entity.client;

import com.application.blank.entity.person.Person;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long clientId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;

    public Client() {
    }

    public Client(Person person, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.person = person;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}

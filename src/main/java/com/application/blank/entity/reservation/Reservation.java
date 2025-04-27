package com.application.blank.entity.reservation;

import com.application.blank.entity.client.Client;
import com.application.blank.entity.room.Prices;
import com.application.blank.entity.room.Room;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name="price_id")
    private Prices price;


    @Column(updatable = false)
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;
    @Column
    private int reservationDaysAmount;

    @Column
    private BigDecimal deposit;
    @OneToMany (mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payments> payments;
    @Column
    private BigDecimal total;

    @Column
    private boolean ended;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        startDate = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Reservation() {
    }

    public Reservation(Client client, Room room, Prices price, LocalDateTime startDate, LocalDateTime endDate, int reservationDaysAmount, BigDecimal deposit, List<Payments> payments, BigDecimal total, boolean ended, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.client = client;
        this.room = room;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationDaysAmount = reservationDaysAmount;
        this.deposit = deposit;
        this.payments = payments;
        this.total = total;
        this.ended = ended;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getReservationDaysAmount() {
        return reservationDaysAmount;
    }

    public void setReservationDaysAmount(int reservationDaysAmount) {
        this.reservationDaysAmount = reservationDaysAmount;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Prices getPrice() {
        return price;
    }

    public void setPrice(Prices price) {
        this.price = price;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
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

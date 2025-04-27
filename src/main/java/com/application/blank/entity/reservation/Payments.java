package com.application.blank.entity.reservation;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long paymentsId;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column
    private BigDecimal amount;
    @Column
    private boolean paid;

    public Payments() {
    }

    public Payments(Reservation reservation, BigDecimal amount, boolean paid) {
        this.reservation = reservation;
        this.amount = amount;
        this.paid = paid;
    }

    public Long getPaymentsId() {
        return paymentsId;
    }

    public void setPaymentsId(Long paymentsId) {
        this.paymentsId = paymentsId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

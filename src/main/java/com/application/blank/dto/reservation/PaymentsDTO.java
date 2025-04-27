package com.application.blank.dto.reservation;


import java.math.BigDecimal;

public class PaymentsDTO {
    private Long paymentsId;

    private Long reservation;
    private BigDecimal amount;
    private boolean paid;

    public PaymentsDTO() {
    }

    public PaymentsDTO(Long paymentsId, Long reservation, BigDecimal amount, boolean paid) {
        this.paymentsId = paymentsId;
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

    public Long getReservation() {
        return reservation;
    }

    public void setReservation(Long reservation) {
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

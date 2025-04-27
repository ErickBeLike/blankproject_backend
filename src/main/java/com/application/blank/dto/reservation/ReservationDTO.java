package com.application.blank.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationDTO {
    private Long reservationId;
    private Long client;

    private Long room;
    private Long price;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int reservationDaysAmount;

    private BigDecimal deposit;
    private List<PaymentsDTO> payments;
    private BigDecimal total;

    private boolean ended;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public ReservationDTO() {
    }

    public ReservationDTO(Long reservationId, Long client, Long room, Long price, LocalDateTime startDate, LocalDateTime endDate, int reservationDaysAmount, BigDecimal deposit, List<PaymentsDTO> payments, BigDecimal total, boolean ended, LocalDateTime createAt, LocalDateTime updateAt) {
        this.reservationId = reservationId;
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
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getClient() {
        return client;
    }

    public void setClient(Long client) {
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

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public List<PaymentsDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentsDTO> payments) {
        this.payments = payments;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}

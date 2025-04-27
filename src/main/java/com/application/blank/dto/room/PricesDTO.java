package com.application.blank.dto.room;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PricesDTO {
    private Long   priceId;

    private String priceName;
    private int daysAmount;
    private BigDecimal price;
    private BigDecimal deposit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PricesDTO() {
    }

    public PricesDTO(Long priceId,
                     String priceName,
                     int daysAmount,
                     BigDecimal price,
                     BigDecimal deposit,
                     LocalDateTime createdAt,
                     LocalDateTime updatedAt) {
        this.priceId = priceId;
        this.priceName = priceName;
        this.daysAmount = daysAmount;
        this.price = price;
        this.deposit = deposit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public int getDaysAmount() {
        return daysAmount;
    }

    public void setDaysAmount(int daysAmount) {
        this.daysAmount = daysAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
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

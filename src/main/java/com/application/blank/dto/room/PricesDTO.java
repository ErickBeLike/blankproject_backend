package com.application.blank.dto.room;

import java.time.LocalDateTime;

public class PricesDTO {
    private Long   priceId;

    private String priceName;
    private Double price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PricesDTO() {
    }

    public PricesDTO(Long priceId, String priceName, Double price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.priceId = priceId;
        this.priceName = priceName;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

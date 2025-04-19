package com.application.blank.dto.room;

import com.application.blank.entity.room.RoomStatus;
import com.application.blank.entity.room.RoomType;

import java.time.LocalDateTime;
import java.util.List;

public class RoomDTO {
    private Long roomId;
    private RoomStatus roomStatus;
    private String roomName;
    private String roomDescription;
    private RoomType roomType;
    private int capacity;
    private int floor;
    private int roomNumber;
    private List<PricesDTO> prices;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoomDTO() {
    }

    public RoomDTO(Long roomId,
                   RoomStatus roomStatus,
                   String roomName,
                   String roomDescription,
                   RoomType roomType,
                   int capacity,
                   int floor,
                   int roomNumber,
                   List<PricesDTO> prices,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.roomId = roomId;
        this.roomStatus = roomStatus;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomType = roomType;
        this.capacity = capacity;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.prices = prices;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<PricesDTO> getPrices() {
        return prices;
    }

    public void setPrices(List<PricesDTO> prices) {
        this.prices = prices;
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

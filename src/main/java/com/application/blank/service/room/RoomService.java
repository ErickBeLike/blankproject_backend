package com.application.blank.service.room;

import com.application.blank.dto.room.RoomDTO;
import com.application.blank.dto.room.PricesDTO;
import com.application.blank.entity.room.Prices;
import com.application.blank.entity.room.Room;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.room.PricesRepository;
import com.application.blank.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PricesRepository pricesRepository;

    // Retrieve all rooms as DTOs
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a single room by ID
    public RoomDTO getRoomById(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));
        return mapToDTO(room);
    }

    // Create a new room
    public RoomDTO saveRoom(RoomDTO roomDTO) throws ResourceNotFoundException {
        Room room = mapToEntity(roomDTO);
        return mapToDTO(roomRepository.save(room));
    }

    // Update an existing room
    public RoomDTO updateRoom(Long roomId, RoomDTO roomDTO) throws ResourceNotFoundException {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));

        existingRoom.setRoomStatus(roomDTO.getRoomStatus());
        existingRoom.setRoomName(roomDTO.getRoomName());
        existingRoom.setRoomDescription(roomDTO.getRoomDescription());
        existingRoom.setRoomType(roomDTO.getRoomType());
        existingRoom.setCapacity(roomDTO.getCapacity());
        existingRoom.setFloor(roomDTO.getFloor());
        existingRoom.setRoomNumber(roomDTO.getRoomNumber());
        existingRoom.setUpdatedAt(java.time.LocalDateTime.now());

        return mapToDTO(roomRepository.save(existingRoom));
    }

    // Delete a room
    public Map<String, Boolean> deleteRoom(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));
        roomRepository.delete(room);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // Mapping from Room entity to DTO
    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomStatus(room.getRoomStatus());
        dto.setRoomName(room.getRoomName());
        dto.setRoomDescription(room.getRoomDescription());
        dto.setRoomType(room.getRoomType());
        dto.setCapacity(room.getCapacity());
        dto.setFloor(room.getFloor());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());

        List<PricesDTO> pricesDTOs = room.getPrices().stream().map(price -> {
            PricesDTO pDto = new PricesDTO();
            pDto.setPriceId(price.getPriceId());
            pDto.setPriceName(price.getPriceName());
            pDto.setPrice(price.getPrice());
            pDto.setCreatedAt(price.getCreatedAt());
            pDto.setUpdatedAt(price.getUpdatedAt());
            return pDto;
        }).collect(Collectors.toList());
        dto.setPrices(pricesDTOs);

        return dto;
    }

    // Mapping from DTO to Room entity
    private Room mapToEntity(RoomDTO dto) throws ResourceNotFoundException {
        Room room = new Room();
        room.setRoomId(dto.getRoomId());
        room.setRoomStatus(dto.getRoomStatus());
        room.setRoomName(dto.getRoomName());
        room.setRoomDescription(dto.getRoomDescription());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setFloor(dto.getFloor());
        room.setRoomNumber(dto.getRoomNumber());

        if (dto.getPrices() != null) {
            List<Prices> prices = new ArrayList<>();
            for (PricesDTO pDto : dto.getPrices()) {
                Prices price = pricesRepository.findById(pDto.getPriceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: " + pDto.getPriceId()));
                prices.add(price);
            }
            room.setPrices(prices);
        }

        return room;
    }
}

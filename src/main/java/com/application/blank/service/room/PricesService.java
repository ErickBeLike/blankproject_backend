package com.application.blank.service.room;

import com.application.blank.dto.room.PricesDTO;
import com.application.blank.entity.room.Prices;
import com.application.blank.entity.room.Room;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.room.PricesRepository;
import com.application.blank.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PricesService {

    @Autowired
    private PricesRepository pricesRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Create a new price and associate it with a room
    public PricesDTO createPrice(Long roomId, PricesDTO dto) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));

        Prices price = new Prices();
        price.setPriceName(dto.getPriceName());
        price.setPrice(dto.getPrice());
        price.setRoom(room);

        return mapToDTO(pricesRepository.save(price));
    }

    // Update an existing price
    public PricesDTO updatePrice(Long priceId, PricesDTO dto) throws ResourceNotFoundException {
        Prices price = pricesRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: " + priceId));

        price.setPriceName(dto.getPriceName());
        price.setPrice(dto.getPrice());

        return mapToDTO(pricesRepository.save(price));
    }

    // Delete a price
    public void deletePrice(Long priceId) throws ResourceNotFoundException {
        Prices price = pricesRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: " + priceId));
        pricesRepository.delete(price);
    }

    // Get all prices for a specific room
    public List<PricesDTO> getPricesByRoomId(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));
        return room.getPrices().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PricesDTO mapToDTO(Prices price) {
        PricesDTO dto = new PricesDTO();
        dto.setPriceId(price.getPriceId());
        dto.setPriceName(price.getPriceName());
        dto.setPrice(price.getPrice());
        dto.setCreatedAt(price.getCreatedAt());
        dto.setUpdatedAt(price.getUpdatedAt());
        return dto;
    }
}

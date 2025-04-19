package com.application.blank.controller.room;

import com.application.blank.dto.room.PricesDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.room.PricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class PricesController {

    @Autowired
    private PricesService pricesService;

    // Get all prices for a specific room
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roomId}/prices")
    public ResponseEntity<List<PricesDTO>> getPricesByRoomId(@PathVariable Long roomId) throws ResourceNotFoundException {
        List<PricesDTO> prices = pricesService.getPricesByRoomId(roomId);
        return ResponseEntity.ok(prices);
    }

    // Create a new price for a specific room
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{roomId}/prices")
    public ResponseEntity<PricesDTO> createPrice(@PathVariable Long roomId, @RequestBody PricesDTO pricesDTO)
            throws ResourceNotFoundException {
        PricesDTO newPrice = pricesService.createPrice(roomId, pricesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPrice);
    }

    // Update an existing price
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/prices/{priceId}")
    public ResponseEntity<PricesDTO> updatePrice(@PathVariable Long priceId, @RequestBody PricesDTO pricesDTO)
            throws ResourceNotFoundException {
        PricesDTO updatedPrice = pricesService.updatePrice(priceId, pricesDTO);
        return ResponseEntity.ok(updatedPrice);
    }

    // Delete a price
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/prices/{priceId}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long priceId) throws ResourceNotFoundException {
        pricesService.deletePrice(priceId);
        return ResponseEntity.noContent().build();
    }
}

package com.application.blank.controller.reservation;

import com.application.blank.dto.reservation.ReservationDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) throws ResourceNotFoundException {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO)
            throws ResourceNotFoundException {
        ReservationDTO created = reservationService.saveReservation(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationDTO reservationDTO
    ) throws ResourceNotFoundException {
        ReservationDTO updated = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteReservation(@PathVariable Long id)
            throws ResourceNotFoundException {
        Map<String, Boolean> resp = reservationService.deleteReservation(id);
        return ResponseEntity.ok(resp);
    }
}

package com.application.blank.controller.room;

import com.application.blank.dto.room.RoomDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) throws ResourceNotFoundException {
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok().body(room);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomDTO> saveRoom(@RequestBody RoomDTO roomDTO) throws ResourceNotFoundException {
        RoomDTO newRoom = roomService.saveRoom(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDTO)
            throws ResourceNotFoundException {
        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRoom(@PathVariable Long id) throws ResourceNotFoundException {
        Map<String, Boolean> response = roomService.deleteRoom(id);
        return ResponseEntity.ok(response);
    }
}

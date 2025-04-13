package com.application.blank.controller.employee;

import com.application.blank.dto.employee.ScheduleDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.employee.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) throws ResourceNotFoundException {
        ScheduleDTO scheduleDTO = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(scheduleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ScheduleDTO> saveSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO newScheduleDTO = scheduleService.saveSchedule(scheduleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newScheduleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO)
            throws ResourceNotFoundException {
        ScheduleDTO updatedScheduleDTO = scheduleService.updateSchedule(id, scheduleDTO);
        return ResponseEntity.ok(updatedScheduleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteSchedule(@PathVariable Long id) throws ResourceNotFoundException {
        return scheduleService.deleteSchedule(id);
    }
}

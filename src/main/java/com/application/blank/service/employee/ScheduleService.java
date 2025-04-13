package com.application.blank.service.employee;

import com.application.blank.dto.employee.ScheduleDTO;
import com.application.blank.dto.employee.WorkDayDTO;
import com.application.blank.entity.employee.Schedule;
import com.application.blank.entity.employee.WorkDay;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.employee.ScheduleRepository;
import com.application.blank.repository.employee.WorkDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private WorkDayRepository workDayRepository;

    // Get all schedules
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get schedule by ID
    public ScheduleDTO getScheduleById(Long id) throws ResourceNotFoundException {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
        return mapToDTO(schedule);
    }

    // Save a new schedule
    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = mapToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return mapToDTO(savedSchedule);
    }

    // Update an existing schedule
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) throws ResourceNotFoundException {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        existingSchedule.setTitle(scheduleDTO.getTitle());
        existingSchedule.setDescription(scheduleDTO.getDescription());
        existingSchedule.setLocation(scheduleDTO.getLocation());

        // Sync workDays (remove old, add/update new)
        List<WorkDayDTO> updatedWorkDays = scheduleDTO.getWorkDays();

        // Map current workDays by dayOfWeek for easier comparison
        Map<String, WorkDay> currentDaysMap = new HashMap<>();
        for (WorkDay wd : existingSchedule.getWorkDays()) {
            currentDaysMap.put(wd.getDayOfWeek().name(), wd);
        }

        List<WorkDay> newWorkDaysList = new ArrayList<>();

        for (WorkDayDTO incoming : updatedWorkDays) {
            WorkDay existing = currentDaysMap.remove(incoming.getDayOfWeek().name());
            WorkDay workDay = mapWorkDayToEntity(incoming);
            workDay.setSchedule(existingSchedule);

            if (existing != null) {
                // update existing
                existing.setStartTime(workDay.getStartTime());
                existing.setEndTime(workDay.getEndTime());
                newWorkDaysList.add(existing);
            } else {
                // new entry
                newWorkDaysList.add(workDay);
            }
        }

        // any leftover in the map are no longer needed -> delete them
        for (WorkDay toDelete : currentDaysMap.values()) {
            workDayRepository.delete(toDelete);
        }

        // Remove old WorkDays from Schedule (clear the list)
        existingSchedule.getWorkDays().clear();
        existingSchedule.getWorkDays().addAll(newWorkDaysList); // Add updated list

        existingSchedule.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(scheduleRepository.save(existingSchedule));
    }

    // Delete a schedule
    public Map<String, Boolean> deleteSchedule(Long id) throws ResourceNotFoundException {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        scheduleRepository.delete(schedule);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // Private methods to map between entities and DTOs
    private ScheduleDTO mapToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setTitle(schedule.getTitle());
        dto.setDescription(schedule.getDescription());
        dto.setLocation(schedule.getLocation());
        dto.setWorkDays(schedule.getWorkDays().stream()
                .map(this::mapWorkDayToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private Schedule mapToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(dto.getScheduleId());
        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());
        schedule.setLocation(dto.getLocation());
        schedule.setWorkDays(dto.getWorkDays().stream()
                .map(this::mapWorkDayToEntity)
                .collect(Collectors.toList()));
        return schedule;
    }

    private WorkDayDTO mapWorkDayToDTO(WorkDay workDay) {
        WorkDayDTO dto = new WorkDayDTO();
        dto.setWorkDayId(workDay.getWorkDayId());
        dto.setDayOfWeek(workDay.getDayOfWeek());
        dto.setStartTime(workDay.getStartTime());
        dto.setEndTime(workDay.getEndTime());
        return dto;
    }

    private WorkDay mapWorkDayToEntity(WorkDayDTO dto) {
        WorkDay workDay = new WorkDay();
        workDay.setWorkDayId(dto.getWorkDayId());
        workDay.setDayOfWeek(dto.getDayOfWeek());
        workDay.setStartTime(dto.getStartTime());
        workDay.setEndTime(dto.getEndTime());
        return workDay;
    }
}

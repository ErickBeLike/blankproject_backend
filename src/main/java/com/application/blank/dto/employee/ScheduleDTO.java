package com.application.blank.dto.employee;

import java.util.List;

public class ScheduleDTO {

    private Long scheduleId;
    private String title;
    private String description;
    private String location;
    private List<WorkDayDTO> workDays;

    public ScheduleDTO() {
    }

    public ScheduleDTO(Long scheduleId, String title, String description, String location, List<WorkDayDTO> workDays) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.workDays = workDays;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<WorkDayDTO> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<WorkDayDTO> workDays) {
        this.workDays = workDays;
    }
}

package com.application.blank.dto.employee;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class WorkDayDTO {

    private Long workDayId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkDayDTO() {
    }

    public WorkDayDTO(Long workDayId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.workDayId = workDayId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getWorkDayId() {
        return workDayId;
    }

    public void setWorkDayId(Long workDayId) {
        this.workDayId = workDayId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}

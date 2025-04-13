package com.application.blank.repository.employee;

import com.application.blank.entity.employee.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

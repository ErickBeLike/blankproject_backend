package com.application.blank.repository.employee;

import com.application.blank.entity.employee.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
}

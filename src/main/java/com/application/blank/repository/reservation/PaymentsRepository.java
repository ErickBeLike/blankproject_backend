package com.application.blank.repository.reservation;

import com.application.blank.entity.reservation.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}

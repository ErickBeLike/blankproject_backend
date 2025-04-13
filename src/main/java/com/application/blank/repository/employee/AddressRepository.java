package com.application.blank.repository.employee;

import com.application.blank.entity.employee.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

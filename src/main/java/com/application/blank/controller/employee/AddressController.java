package com.application.blank.controller.employee;

import com.application.blank.dto.employee.AddressDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.employee.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public List<AddressDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) throws ResourceNotFoundException {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<AddressDTO> saveAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO newAddressDTO = addressService.saveAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAddressDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO)
            throws ResourceNotFoundException {
        AddressDTO updatedAddressDTO = addressService.updateAddress(id, addressDTO);
        return ResponseEntity.ok(updatedAddressDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteAddress(@PathVariable Long id) throws ResourceNotFoundException {
        return addressService.deleteAddress(id);
    }
}

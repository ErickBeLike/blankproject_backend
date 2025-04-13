package com.application.blank.service.employee;

import com.application.blank.dto.employee.AddressDTO;
import com.application.blank.entity.employee.Address;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.employee.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO getAddressById(Long id) throws ResourceNotFoundException {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));
        return mapToDTO(address);
    }

    public AddressDTO saveAddress(AddressDTO dto) {
        Address saved = addressRepository.save(mapToEntity(dto));
        return mapToDTO(saved);
    }

    public AddressDTO updateAddress(Long id, AddressDTO dto) throws ResourceNotFoundException {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

        existing.setStreet(dto.getStreet());
        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setPostalCode(dto.getPostalCode());
        existing.setCountry(dto.getCountry());

        return mapToDTO(addressRepository.save(existing));
    }

    public Map<String, Boolean> deleteAddress(Long id) throws ResourceNotFoundException {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

        addressRepository.delete(address);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    private AddressDTO mapToDTO(Address entity) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(entity.getAdrressId());
        dto.setStreet(entity.getStreet());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    private Address mapToEntity(AddressDTO dto) {
        Address entity = new Address();
        entity.setAdrressId(dto.getAddressId());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setPostalCode(dto.getPostalCode());
        entity.setCountry(dto.getCountry());
        return entity;
    }
}

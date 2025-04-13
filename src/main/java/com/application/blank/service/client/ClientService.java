package com.application.blank.service.client;

import com.application.blank.dto.client.ClientDTO;
import com.application.blank.entity.client.Client;
import com.application.blank.entity.person.Person;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.client.ClientRepository;
import com.application.blank.repository.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PersonRepository personRepository;

    // Get all clients as DTOs
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a single client by ID
    public ClientDTO getClientById(Long clientId) throws ResourceNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + clientId));
        return mapToDTO(client);
    }

    // Save a new client
    public ClientDTO saveClient(ClientDTO clientDTO) {
        // Create a new Person instance from DTO fields
        Person person = new Person();
        person.setNames(clientDTO.getNames());
        person.setFathersLastName(clientDTO.getFathersLastName());
        person.setMothersLastName(clientDTO.getMothersLastName());
        person.setPhone(clientDTO.getPhone());
        person.setEmail(clientDTO.getEmail());

        // Build Client entity
        Client client = new Client();
        client.setPerson(person);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        // Save and map to DTO
        Client savedClient = clientRepository.save(client);
        return mapToDTO(savedClient);
    }


    // Update an existing client
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) throws ResourceNotFoundException {
        Client existing = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + clientId));

        Person person = existing.getPerson();
        if (person != null) {
            person.setNames(clientDTO.getNames());
            person.setFathersLastName(clientDTO.getFathersLastName());
            person.setMothersLastName(clientDTO.getMothersLastName());
            person.setPhone(clientDTO.getPhone());
            person.setEmail(clientDTO.getEmail());
        } else {
            Person newPerson = new Person();
            newPerson.setNames(clientDTO.getNames());
            newPerson.setFathersLastName(clientDTO.getFathersLastName());
            newPerson.setMothersLastName(clientDTO.getMothersLastName());
            newPerson.setPhone(clientDTO.getPhone());
            newPerson.setEmail(clientDTO.getEmail());
            existing.setPerson(newPerson);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(clientRepository.save(existing));
    }


    // Delete a client by ID
    public Map<String, Boolean> deleteClient(Long clientId) throws ResourceNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + clientId));
        clientRepository.delete(client);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // Map Entity to DTO
    private ClientDTO mapToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setClientId(client.getClientId());

        if (client.getPerson() != null) {
            dto.setNames(client.getPerson().getNames());
            dto.setFathersLastName(client.getPerson().getFathersLastName());
            dto.setMothersLastName(client.getPerson().getMothersLastName());
            dto.setPhone(client.getPerson().getPhone());
            dto.setEmail(client.getPerson().getEmail());
        }

        return dto;
    }

    // Map DTO to Entity
    private Client mapToEntity(ClientDTO dto) {
        // Build Person entity from flat DTO fields
        Person person = new Person();
        person.setNames(dto.getNames());
        person.setFathersLastName(dto.getFathersLastName());
        person.setMothersLastName(dto.getMothersLastName());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());

        // Build Client entity
        Client client = new Client();
        client.setPerson(person);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        return client;
    }

}

package com.application.blank.service.employee;

import com.application.blank.dto.employee.EmployeeDTO;
import com.application.blank.dto.employee.AddressDTO;
import com.application.blank.entity.employee.Address;
import com.application.blank.entity.employee.Employee;
import com.application.blank.entity.employee.Role;
import com.application.blank.entity.employee.Schedule;
import com.application.blank.entity.person.Person;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.employee.EmployeeRepository;
import com.application.blank.repository.employee.RoleRepository;
import com.application.blank.repository.employee.ScheduleRepository;
import com.application.blank.repository.person.PersonRepository;
import com.application.blank.security.entity.User;
import com.application.blank.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    // Obtener todos los empleados como DTOs
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener un empleado por ID como DTO
    public EmployeeDTO getEmployeeById(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for ID: " + employeeId));
        return mapToDTO(employee);
    }

    // Guardar un nuevo empleado a partir del DTO
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) throws ResourceNotFoundException {
        Employee employee = mapToEntity(employeeDTO);

        // Si no hay usuario, no se asigna user
        if (employeeDTO.getUser() == null) {
            employee.setUser(null); // O no asignar nada
        }

        return mapToDTO(employeeRepository.save(employee));
    }


    // Actualizar un empleado existente
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO) throws ResourceNotFoundException {
        // Obtener el empleado existente
        Employee existing = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for ID: " + employeeId));

        // Si el empleado ya tiene una persona asociada, actualizamos esos campos
        Person existingPerson = existing.getPerson();

        // Actualizar los campos de la persona asociada
        if (existingPerson != null) {
            existingPerson.setNames(employeeDTO.getNames());
            existingPerson.setFathersLastName(employeeDTO.getFathersLastName());
            existingPerson.setMothersLastName(employeeDTO.getMothersLastName());
            existingPerson.setPhone(employeeDTO.getPhone());
            existingPerson.setEmail(employeeDTO.getEmail());  // Si el email se actualiza, lo hace aquí
        } else {
            // Si no tiene una persona asociada (esto no debería suceder en la mayoría de los casos)
            Person newPerson = new Person();
            newPerson.setNames(employeeDTO.getNames());
            newPerson.setFathersLastName(employeeDTO.getFathersLastName());
            newPerson.setMothersLastName(employeeDTO.getMothersLastName());
            newPerson.setPhone(employeeDTO.getPhone());
            newPerson.setEmail(employeeDTO.getEmail());
            existing.setPerson(newPerson);  // Asociamos una nueva persona si no hay ninguna
        }

        // Actualizar el resto de los campos del empleado
        existing.setGender(employeeDTO.getGender());
        existing.setDateOfBirth(employeeDTO.getDateOfBirth());
        existing.setCurp(employeeDTO.getCurp());
        existing.setRfc(employeeDTO.getRfc());
        existing.setNss(employeeDTO.getNss());

        // Actualizar la dirección
        if (existing.getAddress() != null) {
            existing.getAddress().setStreet(employeeDTO.getStreet());
            existing.getAddress().setCity(employeeDTO.getCity());
            existing.getAddress().setState(employeeDTO.getState());
            existing.getAddress().setPostalCode(employeeDTO.getPostalCode());
            existing.getAddress().setCountry(employeeDTO.getCountry());
        }

        // Actualizar las relaciones
        existing.setRole(roleRepository.findById(employeeDTO.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for ID: " + employeeDTO.getRole())));
        existing.setSchedule(scheduleRepository.findById(employeeDTO.getSchedule())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for ID: " + employeeDTO.getSchedule())));

        // Si el user no está en el DTO, no se asigna
        if (employeeDTO.getUser() != null) {
            existing.setUser(userRepository.findById(employeeDTO.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + employeeDTO.getUser())));
        }

        // Actualizar la fecha de modificación
        existing.setUpdatedAt(LocalDateTime.now());

        // Guardar el empleado actualizado y devolver el DTO
        return mapToDTO(employeeRepository.save(existing));
    }

    // Eliminar un empleado
    public Map<String, Boolean> deleteEmployee(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for ID: " + employeeId));
        employeeRepository.delete(employee);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // Map Entity to DTO
    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());

        // PERSON
        if (employee.getPerson() != null) {
            dto.setNames(employee.getPerson().getNames());
            dto.setFathersLastName(employee.getPerson().getFathersLastName());
            dto.setMothersLastName(employee.getPerson().getMothersLastName());
            dto.setPhone(employee.getPerson().getPhone());
            dto.setEmail(employee.getPerson().getEmail());
        }

        // GÉNERO Y DATOS PERSONALES
        dto.setGender(employee.getGender());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setCurp(employee.getCurp());
        dto.setRfc(employee.getRfc());
        dto.setNss(employee.getNss());

        // ADDRESS
        if (employee.getAddress() != null) {
            dto.setStreet(employee.getAddress().getStreet());
            dto.setCity(employee.getAddress().getCity());
            dto.setState(employee.getAddress().getState());
            dto.setPostalCode(employee.getAddress().getPostalCode());
            dto.setCountry(employee.getAddress().getCountry());
        }

        // RELACIONES
        dto.setRole(employee.getRole().getRoleId());
        dto.setSchedule(employee.getSchedule().getScheduleId());

        // Verificar si user no es null antes de asignar
        if (employee.getUser() != null) {
            dto.setUser(employee.getUser().getUserId());
        }

        return dto;
    }

    // Map DTO to Entity
    private Employee mapToEntity(EmployeeDTO dto) throws ResourceNotFoundException {
        Role role = roleRepository.findById(dto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for ID: " + dto.getRole()));

        Schedule schedule = scheduleRepository.findById(dto.getSchedule())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for ID: " + dto.getSchedule()));

        // PERSON
        Person person = new Person();
        person.setNames(dto.getNames());
        person.setFathersLastName(dto.getFathersLastName());
        person.setMothersLastName(dto.getMothersLastName());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());

        // ADDRESS
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());

        // EMPLOYEE
        Employee employee = new Employee();
        employee.setPerson(person);
        employee.setGender(dto.getGender());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setCurp(dto.getCurp());
        employee.setRfc(dto.getRfc());
        employee.setNss(dto.getNss());
        employee.setAddress(address);
        employee.setRole(role);
        employee.setSchedule(schedule);

        // Verificar si el user es null antes de asignarlo
        if (dto.getUser() != null) {
            User user = userRepository.findById(dto.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + dto.getUser()));
            employee.setUser(user);
        }

        employee.setUpdatedAt(LocalDateTime.now());

        return employee;
    }

}

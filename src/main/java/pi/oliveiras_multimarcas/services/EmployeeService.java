package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(UUID id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));
        return employee;
    }


    @Transactional
    public Employee insert(EmployeeRequestDTO dto) {
        Employee employee = toEntity(dto);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee = employeeRepository.save(employee);
        return employee;
    }

    @Transactional
    public Employee updateById(UUID id, EmployeeRequestDTO dto) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Employee employee = toEntity(dto);
        employee.setId(id);
        employee.setPassword(passwordEncoder.encode(dto.getPassword())); // Garante que a senha seja atualizada e codificada

        employee = employeeRepository.save(employee);
        return employee;
    }


    @Transactional
    public void deleteById(UUID id){
        if(!employeeRepository.existsById(id)){
            throw new NoSuchException("Usuário");
        }
        employeeRepository.deleteById(id);
    }

    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private Employee toEntity(EmployeeRequestDTO dto) {
        Employee user = new Employee();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setPosition(dto.getPosition());
        user.setContact(dto.getContact());
        return user;
    }
}

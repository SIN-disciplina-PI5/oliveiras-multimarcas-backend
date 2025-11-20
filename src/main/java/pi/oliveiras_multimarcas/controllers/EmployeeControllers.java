package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.DTO.EmployeeResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.services.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employes")
public class EmployeeControllers {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        List<Employee> employees = employeeService.findAll();
        List<EmployeeResponseDTO> userResponseDTOS = employees.stream().map(EmployeeResponseDTO::new).toList();
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable UUID id) {
        try {
            Employee employee = employeeService.findById(id);
            EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO(employee);
            return ResponseEntity.ok().body(employeeResponse);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> insert(@RequestBody EmployeeRequestDTO EmployeeRequestDTO) {
        try {
            if (EmployeeRequestDTO.getName() == null || EmployeeRequestDTO.getEmail() == null || EmployeeRequestDTO.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }
            Employee employee = employeeService.insert(EmployeeRequestDTO);
            EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(employee);
            return ResponseEntity.status(201).body(employeeResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        try {
            Employee employee = employeeService.updateById(id, employeeRequestDTO);
            EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO(employee);
            return ResponseEntity.ok(userResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        try {
            employeeService.deleteById(id);
            return ResponseEntity.ok().body("Usuário deletado");
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
}

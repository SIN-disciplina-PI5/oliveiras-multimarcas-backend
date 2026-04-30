package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.dto.EmployeeResponseDTO;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.services.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
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
        Employee employee = employeeService.findById(id);
        EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO(employee);
        return ResponseEntity.ok().body(employeeResponse);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> insert(@RequestBody EmployeeRequestDTO EmployeeRequestDTO) {
        if (EmployeeRequestDTO.getName() == null || EmployeeRequestDTO.getEmail() == null || EmployeeRequestDTO.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        Employee employee = employeeService.insert(EmployeeRequestDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(employee);
        return ResponseEntity.status(201).body(employeeResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = employeeService.updateById(id, employeeRequestDTO);
        EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO(employee);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok().body("Usuário deletado");
    }
}

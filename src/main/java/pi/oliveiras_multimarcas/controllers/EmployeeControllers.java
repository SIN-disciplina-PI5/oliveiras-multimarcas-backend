package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.dto.ChangePasswordRequestDto;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.dto.EmployeeRequestUpdateDTO;
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

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> findMe(@AuthenticationPrincipal String email) {
        if (email ==null) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Employee employee = employeeService.findByEmail(email);
        EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO(employee);
        return ResponseEntity.ok().body(employeeResponse);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> insert(@RequestBody EmployeeRequestDTO EmployeeRequestDTO) {
        Employee employee = employeeService.insert(EmployeeRequestDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(employee);
        return ResponseEntity.status(201).body(employeeResponseDTO);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal String email,@RequestBody ChangePasswordRequestDto dto){
        Employee employee = employeeService.findByEmail(email);
        employeeService.changePassword(employee.getId(), dto.getPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody EmployeeRequestUpdateDTO employeeRequestDTO) {
        Employee employee = employeeService.updateById(id, employeeRequestDTO);
        EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO(employee);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@AuthenticationPrincipal String email,@RequestBody EmployeeRequestUpdateDTO employeeRequestDTO) {
        Employee employee = employeeService.updateById(employeeService.findByEmail(email).getId(), employeeRequestDTO);
        EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO(employee);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok().body("Usuário deletado");
    }
}

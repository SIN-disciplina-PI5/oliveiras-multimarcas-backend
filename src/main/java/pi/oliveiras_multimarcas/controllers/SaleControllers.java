package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.SaleRequestDTO;
import pi.oliveiras_multimarcas.DTO.SaleResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.services.SaleService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class SaleController {

    @Autowired
    private SaleService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        List<EmployeeResponseDTO> employees = employeeService.findAll();
        List<EmployeeResponseDTO> userResponseDTOS = employees.stream().map(user -> {
            EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return userResponseDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable UUID id) {
        try {
            EmployeeResponseDTO employee = employeeService.findById(id);
            EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO();
            employeeResponse.setId(employee.getId());
            employeeResponse.setName(employee.getName());
            employeeResponse.setEmail(employee.getEmail());
            employeeResponse.setPosition(employee.getPosition());
            return ResponseEntity.ok().body(employeeResponse);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> insert(@RequestBody EmployeeRequestDTO EmployeeRequestDTO) {
        try {
            if (EmployeeRequestDTO.getUsername() == null || EmployeeRequestDTO.getEmail() == null || EmployeeRequestDTO.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }
            EmployeeResponseDTO user = employeeService.insert(EmployeeRequestDTO);
            EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return ResponseEntity.status(201).body(userResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        try {
            EmployeeResponseDTO user = employeeService.updateById(id, employeeRequestDTO);
            EmployeeResponseDTO userResponseDTO = new EmployeeResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
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

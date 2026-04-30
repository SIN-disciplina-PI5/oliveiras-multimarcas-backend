package pi.oliveiras_multimarcas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole; 

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeResponseDTO {
    private UUID id;
    
    private String name; 
    private String email;
    private String position; 
    private UserRole role;
    private String cpf;

    public EmployeeResponseDTO(Employee employee) {
        id = employee.getId();
        name = employee.getName();
        email = employee.getEmail();
        position = employee.getPosition();
        role = employee.getRole();
        cpf = employee.getCpf();
    }
}
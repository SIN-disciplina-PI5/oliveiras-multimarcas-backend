package pi.oliveiras_multimarcas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.Date;
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
    private String profileImage;
    private String contact;
    private String rg;
    private String address;
    private String cityState;
    private Date birthDate;

    public EmployeeResponseDTO(Employee employee) {
        id = employee.getId();
        name = employee.getName();
        email = employee.getEmail();
        position = employee.getPosition();
        role = employee.getRole();
        cpf = employee.getCpf();
        profileImage = employee.getProfileImage();
        contact = employee.getContact();
        rg = employee.getRg();
        address = employee.getAddress();
        cityState = employee.getCityState();
        birthDate = employee.getBirthDate();
    }
}
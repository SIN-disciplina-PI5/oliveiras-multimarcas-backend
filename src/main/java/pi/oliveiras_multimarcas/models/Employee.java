package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Email
    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String position;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Employee(EmployeeRequestDTO dto){
        name = dto.getUsername();
        email = dto.getEmail();
        password = dto.getPassword();
        position = dto.getPosition();
        role = dto.getRole();
    }

}

package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee extends User {

    @Column
    private String position;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public Employee(EmployeeRequestDTO dto){
        super(dto.getName(), dto.getEmail(), dto.getContact(), dto.getCpf());
        position = dto.getPosition();
        password = dto.getPassword();
        role = UserRole.ADMIN;
    }

}

package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee extends User {

    @Column
    private String position;

    public Employee(EmployeeRequestDTO dto){
        super(dto.getName(), dto.getEmail(), dto.getPassword(), UserRole.ADMIN);
        position = dto.getPosition();
    }

}

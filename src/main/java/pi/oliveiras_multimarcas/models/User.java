package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;

import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Email
    @Column
    private String email;
    @Column
    private String password;

    public User(UserRequestDTO dto){
        name = dto.getName();
        email = dto.getEmail();
        password = dto.getPassword();
    }
}

package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole; 

import java.util.Collection;
import java.util.List;
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

    public User(UserRequestDTO dto){
        name = dto.getName(); 
        email = dto.getEmail();
        password = dto.getPassword();
        position = dto.getPosition();
        role = dto.getRole();
    }

}

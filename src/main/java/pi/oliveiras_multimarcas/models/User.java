package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.SignupRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.UUID;

/**
 * Classe abstrata base para todos os usuários do sistema (Funcionários, Clientes).
 * Contém os campos
 * comuns a todos os usuários.
 */
@MappedSuperclass // Indica que esta classe é uma superclasse e seus campos devem ser mapeados nas subclasses
@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * O nome do usuário. Mapeado a partir do 'username' nos DTOs.
     */
    @Column(nullable = false)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column // Opcional, por isso sem 'nullable = false'
    private String contact;
    public User(SignupRequestDTO dto,String contact, UserRole role){
        name = dto.getName();
        email = dto.getEmail();
        password = dto.getPassword();
        this.contact = contact;
        this.role = role;
    }

    public User(String name, String email, String password, UserRole role, String contact){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contact = contact;
    }

}
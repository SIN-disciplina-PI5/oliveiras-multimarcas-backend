package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO; // Certifique-se que este DTO existe ou use o SignupRequestDTO
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.UUID;

@Entity // MUDAMOS DE @MappedSuperclass PARA @Entity
@Table(name = "tb_users") // Damos um nome para a tabela
@Getter
@Setter
@NoArgsConstructor
// REMOVEMOS A PALAVRA 'ABSTRACT'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column
    private String contact;

    // Construtor utilitário para facilitar os testes e o Service
    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
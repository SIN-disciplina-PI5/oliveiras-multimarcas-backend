package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;

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

    @Column(unique = true, nullable = false)
    private String contact;

    @Column(unique = true, nullable = false)
    private String cpf;

    public User(String name, String email, String contact, String cpf){
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.cpf = cpf;
    }

    public User (ClientRequestDTO dto){
        name = dto.getName();
        email = dto.getEmail();
        contact = dto.getContact();
        cpf = dto.getCpf();
    }

}
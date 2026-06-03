package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;
import java.util.Date;

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
public abstract class User extends BaseEntity{

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

    // === NOVOS CAMPOS PARA O CONTRATO ===
    @Column(nullable = true)
    private String rg;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String cityState;

    @Column(nullable = true)
    private Date birthDate;

    public User(String name, String email, String contact, String cpf, String rg, String address, String cityState, Date birthDate){
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.cpf = cpf;
        this.rg = rg;
        this.address = address;
        this.cityState = cityState;
        this.birthDate = birthDate;
    }

    public User (ClientRequestDTO dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.contact = dto.getContact();
        this.cpf = dto.getCpf();
        // Mapeamento dos novos campos
        this.rg = dto.getRg();
        this.address = dto.getAddress();
        this.cityState = dto.getCityState();
        this.birthDate = dto.getBirthDate();
    }
}
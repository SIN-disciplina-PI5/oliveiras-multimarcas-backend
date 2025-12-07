package pi.oliveiras_multimarcas.DTO;

import lombok.Data;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.UUID;

@Data
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private UserRole role; // Mantivemos o Role (Permissão)

    // Construtor vazio (obrigatório para JSON/Lombok)
    public UserResponseDTO() {}

    // Construtor que converte User -> DTO
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
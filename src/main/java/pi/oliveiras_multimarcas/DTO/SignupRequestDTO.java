package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pi.oliveiras_multimarcas.models.enums.UserRole;

@Data
public class SignupRequestDTO {
    @NotBlank(message = "Nome não pode estar nulo")
    private String name;
    @NotBlank(message = "Nome não pode estar nulo")
    private String email;
    @NotBlank(message = "Nome não pode estar nulo")
    private String password;
    @NotBlank(message = "Nome não pode estar nulo")
    private String contact;
}

package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeRequestDTO {

    /**
     * Nome de usuário único para login. Não pode ser nulo ou vazio.
     */
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    private String username;

    /**
     * Endereço de e-mail do usuário. Deve ser um formato de e-mail válido e não pode ser nulo.
     */
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;

    /**
     * Senha de acesso do usuário. Não pode ser nula e deve ter entre 6 e 100 caracteres.
     */
    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 5, max = 20, message = "A senha deve ter entre 5 e 20 caracteres")
    private String password;

    /**
     * Número de contato do usuário (telefone, celular, etc.). Este campo é opcional.
     */
    private String contact;
}

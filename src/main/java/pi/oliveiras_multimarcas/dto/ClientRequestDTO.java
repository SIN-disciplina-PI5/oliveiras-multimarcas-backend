package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequestDTO {

    /**
     * Nome de usuário único para login. Não pode ser nulo ou vazio.
     */
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    private String name;

    /**
     * Endereço de e-mail do usuário. Deve ser um formato de e-mail válido e não pode ser nulo.
     */
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;

    /**
     * Número de contato do usuário (telefone, celular, etc.). Este campo é opcional.
     */
    private String contact;

    @NotBlank(message = "O email não pode estar em branco")
    private String cpf;
}

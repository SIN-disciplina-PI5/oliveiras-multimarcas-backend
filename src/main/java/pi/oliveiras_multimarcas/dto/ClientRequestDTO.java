package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;

@Data
public class ClientRequestDTO {

    @NotBlank(message = "O nome de usuário não pode estar em branco")
    private String name;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "O número não pode estar em branco")
    private String contact;

    @NotBlank(message = "O cpf não pode estar em branco")
    private String cpf;
    private String rg;
    private String address;
    private String cityState;
    private Date birthDate;
}

package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {
    
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;
    
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 5, max = 20, message = "A senha deve ter entre 5 e 20 caracteres")
    private String password;
    
    @NotBlank(message = "O contato não pode estar em branco")
    private String contact;
}
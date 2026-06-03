package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class EmployeeRequestUpdateDTO {
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    private String name;
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;
    private String contact;
    private String profileImage;
    private String position;
}

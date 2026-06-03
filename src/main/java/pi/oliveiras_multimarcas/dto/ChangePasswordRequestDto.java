package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordRequestDto {
    @NotNull
    @Size(min = 8, message = "O campo deve conter pelo menos 8 caracteres")
    private String password;
}

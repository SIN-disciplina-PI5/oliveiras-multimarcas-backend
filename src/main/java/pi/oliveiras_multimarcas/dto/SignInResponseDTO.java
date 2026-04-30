package pi.oliveiras_multimarcas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class SignInResponseDTO {
    private String refreshToken; 
    private String acessToken;  
}
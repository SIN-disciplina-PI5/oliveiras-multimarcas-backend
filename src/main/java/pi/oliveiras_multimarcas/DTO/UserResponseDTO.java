package pi.oliveiras_multimarcas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.User; 
import pi.oliveiras_multimarcas.models.enums.UserPosition; 

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String email;
    private UserPosition position; 

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.position = user.getPosition();
    }
}
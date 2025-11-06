package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.SignupRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client extends User{

    public Client(SignupRequestDTO dto){
        super(dto, UserRole.USER);
    }

}

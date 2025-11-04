package pi.oliveiras_multimarcas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.enums.UserRole; 

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleResponseDTO {
    private UUID id;
    
    private String name; 
    private String email;
    private String position; 
    private UserRole role; 

    public SaleResponseDTO(Sale Sale) {
        this.id = Sale.getId();
        this.name = Sale.getName();
        this.email = Sale.getEmail();
        this.position = Sale.getPosition();
        this.role = Sale.getRole();
    }
}
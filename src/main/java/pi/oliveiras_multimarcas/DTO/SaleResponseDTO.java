package pi.oliveiras_multimarcas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleResponseDTO {
    private UUID id;
    
    private Client client;
    private Vehicle vehicle;
    private Date saleDate;

    public SaleResponseDTO(Sale sale) {
        this.id = sale.getId();
        this.client = sale.getClient();
        this.vehicle = sale.getVehicle();
        this.saleDate = sale.getSaleDate();
    }
}
package pi.oliveiras_multimarcas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Sale;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleResponseDTO {
    private UUID id;
    
    private UUID client;
    private UUID vehicle;
    private Date saleDate;

    public SaleResponseDTO(Sale sale) {
        this.id = sale.getId();
        this.client = sale.getClient().getId();
        this.vehicle = sale.getVehicle().getId();
        this.saleDate = sale.getSaleDate();
    }
}
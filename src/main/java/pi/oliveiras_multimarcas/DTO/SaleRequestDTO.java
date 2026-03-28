package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class SaleRequestDTO {

    @NotNull(message = "A data da venda é obrigatória")
    @PastOrPresent(message = "A data da venda não pode estar no futuro")
    private Date saleDate;

    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID client;

    @NotNull(message = "O ID do veículo é obrigatório")
    private UUID vehicle;
}

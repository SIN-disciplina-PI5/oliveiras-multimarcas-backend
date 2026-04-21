package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SaleRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID client;

    @NotNull(message = "O ID do veículo é obrigatório")
    private UUID vehicle;
}

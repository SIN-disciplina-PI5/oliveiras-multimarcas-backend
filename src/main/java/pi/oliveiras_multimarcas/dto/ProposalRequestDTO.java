package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProposalRequestDTO {
    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID clientId;

    @NotNull(message = "O ID do veículo é obrigatório")
    private UUID vehicleId;

    @NotNull(message = "O valor de entrada é obrigatório")
    @Min(value = 0, message = "O valor de entrada não pode ser negativo")
    private BigDecimal entryValue;

    @NotNull(message = "A quantidade de parcelas é obrigatória")
    @Min(value = 1, message = "A quantidade de parcelas deve ser pelo menos 1")
    private int installments;

    // A taxa de juros pode ser recebida do front-end ou fixada no backend.
    private BigDecimal interestRate;

    private String observations;
}
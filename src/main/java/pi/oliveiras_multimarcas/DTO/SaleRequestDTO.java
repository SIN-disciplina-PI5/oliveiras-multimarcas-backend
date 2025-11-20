package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class SaleRequestDTO {

    @NotBlank
    private Date saleDate;

    @NotBlank
    private UUID client;

    @NotBlank
    private UUID vehicle;
}

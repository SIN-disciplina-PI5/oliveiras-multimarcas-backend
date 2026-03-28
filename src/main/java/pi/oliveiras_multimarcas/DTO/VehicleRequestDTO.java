package pi.oliveiras_multimarcas.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDTO {

    @NotBlank(message = "O modelo é obrigatório")
    private String model;

    @NotNull(message = "O ano do modelo é obrigatório")
    private Integer modelYear;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal price;

    @NotEmpty(message = "É necessário enviar pelo menos uma URL de imagem")
    private List<String> url_images;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "A quilometragem é obrigatória")
    @Min(value = 0, message = "A quilometragem não pode ser negativa")
    private Integer mileage;

    @NotBlank(message = "A marca é obrigatória")
    private String mark;
}

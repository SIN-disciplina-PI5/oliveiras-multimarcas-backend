package pi.oliveiras_multimarcas.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDTO {

    private String model;
    private int year;
    private BigDecimal price;
    private List<String> url_images;
    private String description;
    private int mileage;
    private String mark;
}

package pi.oliveiras_multimarcas.DTO;

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
    private String year;
    private int price;
    private List<String> url_images;
    private String description;
    private int mileage;
    private String mark;
}

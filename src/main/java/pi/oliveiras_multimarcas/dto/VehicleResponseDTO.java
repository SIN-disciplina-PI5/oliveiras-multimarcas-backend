package pi.oliveiras_multimarcas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.ImageVehicle;
import pi.oliveiras_multimarcas.models.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {
    private UUID id;
    private String model;
    private int modelYear;
    private BigDecimal price;
    private List<String> url_images = new ArrayList<String>();
    private String description;
    private int mileage;
    private String mark;

    public VehicleResponseDTO(Vehicle vehicle){
        this.id = vehicle.getId();
        this.model = vehicle.getModel();
        this.modelYear = vehicle.getModelYear();
        this.price = vehicle.getPrice();

        for (ImageVehicle imageVehicle : vehicle.getUrl_images()){
            url_images.add(imageVehicle.getUrl());
        }

        this.description = vehicle.getDescription();
        this.mileage = vehicle.getMileage();
        this.mark = vehicle.getMark();

    }
}

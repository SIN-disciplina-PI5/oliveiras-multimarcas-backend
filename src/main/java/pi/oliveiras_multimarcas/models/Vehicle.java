package pi.oliveiras_multimarcas.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="veiculos")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String model;
    @Column(nullable=false)
    private int modelYear;
    @Column(nullable=false, precision = 10, scale = 2)
    private BigDecimal price;
    @OneToMany(mappedBy="vehicle", cascade=CascadeType.ALL)
    private List<ImageVehicle> url_images = new ArrayList<ImageVehicle>();
    @Column(nullable=true)
    private String description;
    @Column(nullable=false)
    private int mileage;
    @Column(nullable=false)
    private String mark;

    public Vehicle(VehicleRequestDTO dto){
        this.model = dto.getModel();

        // Ano do carro não deve ser menor que 15 anos do ano atual nem maior que o ano atual
        Calendar now = Calendar.getInstance();
        if (dto.getModelYear()<now.get(Calendar.YEAR)-15||dto.getModelYear()>now.get(Calendar.YEAR)) {
            throw new InvalidArguments("ano");
        }
        this.modelYear = dto.getModelYear();

        // preço do carro não pode ser igual ou menor que zero
        int compare = dto.getPrice().compareTo(new BigDecimal(0));
        if ( compare == -1 || compare == 0) {
            throw new InvalidArguments("preço");
        }
        this.price = dto.getPrice();

        for (String url : dto.getUrl_images()) {
            ImageVehicle image_url = new ImageVehicle(url, this);
            this.url_images.add(image_url);
        }

        this.description = dto.getDescription();
        // Quilometragem não pode ser negativa
        if(dto.getMileage()<0){
            throw new InvalidArguments("quilometragem");
        }
        this.mileage = dto.getMileage();
        this.mark = dto.getMark();
    }

    public void setModelYear(int modelYear){
        Calendar now = Calendar.getInstance();
        if (modelYear<now.get(Calendar.YEAR)-15||modelYear>now.get(Calendar.YEAR)) {
            throw new InvalidArguments("ano");
        }
        this.modelYear = modelYear;
    }
}

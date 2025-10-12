package pi.oliveiras_multimarcas.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private int year;
    @Column(nullable=false)
    private BigDecimal price;
    @Column(nullable=true)
    private List<String> url_images;
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
        if (dto.getYear()<now.get(Calendar.YEAR)-15||dto.getYear()>now.get(Calendar.YEAR)) {
            throw new InvalidArguments("ano");
        }
        this.year = dto.getYear();

        // preço do carro não pode ser igual ou menor que zero 
        int compare = dto.getPrice().compareTo(new BigDecimal(0));
        if ( compare == -1 || compare == 0) {
            throw new InvalidArguments("preço");
        }
        this.price = dto.getPrice();
        
        this.url_images = new ArrayList<>();
        for (String url : dto.getUrl_images()) {
            
            this.url_images.add(url);
        }
        
        this.description = dto.getDescription();
        // Quilometragem não pode ser negativa
        if(dto.getMileage()<0){
            throw new InvalidArguments("quilometragem");
        }
        this.mileage = dto.getMileage();
        this.mark = dto.getMark();
    }

}

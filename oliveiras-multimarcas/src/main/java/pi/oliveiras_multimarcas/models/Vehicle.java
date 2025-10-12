package pi.oliveiras_multimarcas.models;

import java.util.List;

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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="veiculos")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable=false)
    private String model;
    @Column(nullable=false)
    private String year;
    @Column(nullable=false)
    private int price;
    @Column(nullable=true)
    private List<String> url_image;
    @Column(nullable=true)
    private String description;
    @Column(nullable=false)
    private int mileage;
    @Column(nullable=false)
    private String mark;

    public Vehicle(){
        
    }
}

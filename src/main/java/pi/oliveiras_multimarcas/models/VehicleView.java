package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CarView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private Date dateTime = new Date();

    @ManyToOne
    @JoinColumn(name="vehicle_id", nullable = false)
    private Vehicle vehicle;

    public CarView(Vehicle vehicle){
        this.vehicle = vehicle;
    }

}

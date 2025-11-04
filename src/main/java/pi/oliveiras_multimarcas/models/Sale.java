package pi.oliveiras_multimarcas.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String url;

    @ManyToOne
    @JoinColumn(name="vehicle_id", nullable=true)
    private Vehicle vehicle;

    public Sale (Vehicle vehicle, Cliente cliente, Sale_date date){
        this.vehicle = vehicle;
        this.cliente = cliente;
        this.sale_date = date;
    }
}
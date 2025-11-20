package pi.oliveiras_multimarcas.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=true)
    private Client client;
    @Column
    private Date saleDate;
    @ManyToOne
    @JoinColumn(name="vehicle_id", nullable=true)
    private Vehicle vehicle;

    public Sale (Vehicle vehicle, Client client, Date saleDate){
        this.vehicle = vehicle;
        this.client = client;
        this.saleDate = saleDate;
    }


}
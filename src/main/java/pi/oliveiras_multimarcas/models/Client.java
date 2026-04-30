package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Client extends User{

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;
    public Client(ClientRequestDTO dto){
        super(dto);
    }

}

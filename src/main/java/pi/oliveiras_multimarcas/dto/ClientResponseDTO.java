package pi.oliveiras_multimarcas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Client;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientResponseDTO {
    private UUID id;
    private String name; 
    private String email;
    private String contact;
    private String cpf;

    public ClientResponseDTO(Client client) {
        id = client.getId();
        name = client.getName();
        email = client.getEmail();
        contact = client.getContact();
        cpf = client.getCpf();
    }
}
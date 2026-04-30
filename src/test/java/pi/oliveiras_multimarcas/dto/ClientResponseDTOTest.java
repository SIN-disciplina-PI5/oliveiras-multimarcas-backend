package pi.oliveiras_multimarcas.dto;

import org.junit.jupiter.api.Test;
import pi.oliveiras_multimarcas.models.Client;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientResponseDTOTest {

    @Test
    void deveCriarDTOCorretamente() {
      
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("João");
        client.setEmail("joao@teste.com");

        ClientResponseDTO dto = new ClientResponseDTO(client);

        
        assertEquals(client.getId(), dto.getId());
        assertEquals(client.getName(), dto.getName());
        assertEquals(client.getEmail(), dto.getEmail());
    }
}

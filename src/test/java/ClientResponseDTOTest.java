package pi.oliveiras_multimarcas.DTO;

import org.junit.jupiter.api.Test;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.enums.UserRole;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientResponseDTOTest {

    @Test
    void deveCriarDTOCorretamente() {
      
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("João");
        client.setEmail("joao@teste.com");
        client.setRole(UserRole.USER); 

        ClientResponseDTO dto = new ClientResponseDTO(client);

        
        assertEquals(client.getId(), dto.getId());
        assertEquals(client.getName(), dto.getName());
        assertEquals(client.getEmail(), dto.getEmail());
        assertEquals(client.getRole(), dto.getRole());
    }
}

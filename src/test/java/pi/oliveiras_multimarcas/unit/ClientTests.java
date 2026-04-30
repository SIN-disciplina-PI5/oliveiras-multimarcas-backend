package pi.oliveiras_multimarcas.unit; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pi.oliveiras_multimarcas.dto.ClientRequestDTO;
import pi.oliveiras_multimarcas.models.Client;

public class ClientTests {

    @Test
    void deveCriarClientCorretamenteAPartirDoDTO() {
        // Arrange (dados de entrada)
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setName("Marília");
        dto.setEmail("marilia@example.com");
        dto.setCpf("12345678912");
        dto.setContact("81900000006");

        // Act (ação)
        Client client = new Client(dto);

        // Assert (validações)
        assertNotNull(client);
        assertEquals("Marília", client.getName());
        assertEquals("marilia@example.com", client.getEmail());
        assertEquals("81900000006", client.getContact());
    }

    @Test
    void deveAlterarNomeDoClient() {
        ClientRequestDTO dto = new ClientRequestDTO();
        Client client = new Client(dto);

        client.setName("Novo Nome");

        assertEquals("Novo Nome", client.getName());
    }
}

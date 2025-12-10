package pi.oliveiras_multimarcas.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pi.oliveiras_multimarcas.DTO.SignupRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;

public class ClientTests {

    @Test
    void deveCriarClientCorretamenteAPartirDoDTO() {
        // Arrange (dados de entrada)
        SignupRequestDTO dto = new SignupRequestDTO();
        dto.setName("Marília");
        dto.setEmail("marilia@example.com");
        dto.setPassword("senha123");

        String contact = "81999999999";

        // Act (ação)
        Client client = new Client(dto, contact);

        // Assert (validações)
        assertNotNull(client);
        assertEquals("Marília", client.getName());
        assertEquals("marilia@example.com", client.getEmail());
        assertEquals("senha123", client.getPassword());
        assertEquals(contact, client.getContact());
        assertEquals(UserRole.USER, client.getRole());
    }

    // ESTE É O NOVO TESTE
    @Test
    void deveAlterarNomeDoClient() {
        SignupRequestDTO dto = new SignupRequestDTO();
        Client client = new Client(dto, "81999999999");

        client.setName("Novo Nome");

        assertEquals("Novo Nome", client.getName());
    }
}

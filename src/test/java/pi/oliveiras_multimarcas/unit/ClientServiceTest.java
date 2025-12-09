package pi.oliveiras_multimarcas.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.ClientRepository;
import pi.oliveiras_multimarcas.services.ClientService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        client.setName("Cliente Teste");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Client result = clientService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Cliente Teste", result.getName());
    }

    @Test
    void testInsertClientSuccess() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setUsername("Novo Cliente");
        dto.setEmail("novo@email.com");
        dto.setPassword("123456");
        dto.setRole(UserRole.USER);

        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID());
        savedClient.setName("Novo Cliente");

        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Client result = clientService.insert(dto);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(any(Client.class));
    }
}

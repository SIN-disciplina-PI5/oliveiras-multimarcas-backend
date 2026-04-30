package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.services.ClientService;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.TokenService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClientControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    // Mocks adicionais para o contexto de segurança
    @Mock private TokenService tokenService;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void testInsertClientEndpoint() throws Exception {
        // Preparação do DTO de entrada
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setName("Mock Teste");
        dto.setEmail("mock@teste.com");
        dto.setCpf("12345678901");
        dto.setContact("81900000009");
        // Preparação do objeto Client e DTO de resposta simulados
        Client clientMock = new Client(dto);
        clientMock.setId(UUID.randomUUID());


        // Simulando o serviço
        when(clientService.insert(any(ClientRequestDTO.class))).thenReturn(clientMock);

        // Executando o teste
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
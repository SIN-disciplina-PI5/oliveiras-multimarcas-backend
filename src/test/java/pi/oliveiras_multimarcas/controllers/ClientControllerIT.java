package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.ClientRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /clients - Deve retornar lista de clientes quando existirem registros")
    void findAll_ShouldReturnList_WhenClientsExist() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@email.com");
        client.setPassword("password");
        client.setRole(UserRole.USER);
        clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Client"))
                .andExpect(jsonPath("$[0].email").value("test@email.com"));
    }

    @Test
    @DisplayName("GET /clients - Deve retornar lista vazia quando não houver registros")
    void findAll_ShouldReturnEmptyList_WhenNoClients() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /clients/{id} - Deve retornar cliente quando ID existir")
    void findById_ShouldReturnClient_WhenIdExists() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@email.com");
        client.setPassword("password");
        client.setRole(UserRole.USER);
        Client savedClient = clientRepository.save(client);

        // Act & Assert
        mockMvc.perform(get("/clients/{id}", savedClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedClient.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test Client"));
    }

    @Test
    @DisplayName("GET /clients/{id} - Deve retornar 404 quando ID não existir")
    void findById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /clients - Deve retornar 201 para payload válido e verificar criptografia")
    void insert_ShouldReturn201_WhenPayloadIsValid() throws Exception {
        // Arrange
        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setUsername("New Client");
        requestDTO.setEmail("new@email.com");
        requestDTO.setPassword("secret123");
        requestDTO.setRole(UserRole.USER);

        // Act
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Client"))
                .andExpect(jsonPath("$.email").value("new@email.com"));

        // Assert Persistence & Encryption
        Client savedClient = clientRepository.findByEmail("new@email.com").orElseThrow();
        assertNotEquals("secret123", savedClient.getPassword(), "A senha deve estar criptografada");
        assertEquals("New Client", savedClient.getName());
    }

    @Test
    @DisplayName("POST /clients - Deve retornar 400 se faltar username, email ou password")
    void insert_ShouldReturn400_WhenRequiredFieldsMissing() throws Exception {
        // Missing Username
        ClientRequestDTO noName = new ClientRequestDTO();
        noName.setEmail("valid@email.com");
        noName.setPassword("password");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noName)))
                .andExpect(status().isBadRequest());

        // Missing Email
        ClientRequestDTO noEmail = new ClientRequestDTO();
        noEmail.setUsername("User");
        noEmail.setPassword("password");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /clients - Deve retornar 400 para email inválido")
    void insert_ShouldReturn400_WhenEmailIsInvalid() throws Exception {
        // Note: Assuming Controller has validation enabled for @Email
        ClientRequestDTO invalidEmail = new ClientRequestDTO();
        invalidEmail.setUsername("User");
        invalidEmail.setEmail("invalid-email"); // No @ or domain
        invalidEmail.setPassword("password");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /clients - Deve retornar 400 para senha curta")
    void insert_ShouldReturn400_WhenPasswordIsShort() throws Exception {
        // Note: Assuming Controller has validation enabled for @Size
        ClientRequestDTO shortPass = new ClientRequestDTO();
        shortPass.setUsername("User");
        shortPass.setEmail("valid@email.com");
        shortPass.setPassword("123"); // Min is 5

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortPass)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /clients/{id} - Deve retornar 200 e atualizar cliente válido")
    void updateById_ShouldReturn200_WhenValid() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("Old Name");
        client.setEmail("old@email.com");
        client.setPassword("oldpass");
        client.setRole(UserRole.USER);
        Client saved = clientRepository.save(client);

        ClientRequestDTO updateDTO = new ClientRequestDTO();
        updateDTO.setUsername("Updated Name");
        updateDTO.setEmail("updated@email.com");
        updateDTO.setPassword("newpass");
        updateDTO.setRole(UserRole.USER);

        // Act
        mockMvc.perform(put("/clients/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@email.com"));

        // Assert DB
        Client updated = clientRepository.findById(saved.getId()).get();
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @DisplayName("PUT /clients/{id} - Deve retornar 404 quando ID não existir")
    void updateById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        ClientRequestDTO updateDTO = new ClientRequestDTO();
        updateDTO.setUsername("Name");
        updateDTO.setEmail("email@test.com");
        updateDTO.setPassword("password");

        mockMvc.perform(put("/clients/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /clients/{id} - Deve retornar 200 ao deletar cliente existente")
    void deleteById_ShouldReturn200_WhenIdExists() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("To Delete");
        client.setEmail("delete@email.com");
        client.setPassword("password");
        client.setRole(UserRole.USER);
        Client saved = clientRepository.save(client);

        // Act
        mockMvc.perform(delete("/clients/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado"));

        // Assert
        assertFalse(clientRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("DELETE /clients/{id} - Deve retornar 404 se o ID não existir")
    void deleteById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}

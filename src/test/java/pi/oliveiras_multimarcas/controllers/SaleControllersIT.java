package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;
import pi.oliveiras_multimarcas.dto.VehicleRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.repositories.SaleRepository;
import pi.oliveiras_multimarcas.services.ClientService;
import pi.oliveiras_multimarcas.services.VehicleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SaleControllersIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID clientId;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();

        // Cria um Cliente válido
        ClientRequestDTO clientDTO = new ClientRequestDTO();
        clientDTO.setName("Cliente Venda");
        clientDTO.setEmail("venda_" + UUID.randomUUID().toString().substring(0, 8) + "@teste.com");
        clientDTO.setContact("8199" + String.format("%07d", (int)(Math.random() * 10000000)));
        clientDTO.setCpf(String.format("%011d", (long)(Math.random() * 100000000000L)));
        Client savedClient = clientService.insert(clientDTO);
        this.clientId = savedClient.getId();

        // Cria um Veículo válido
        VehicleRequestDTO vehicleDTO = new VehicleRequestDTO(
                "Gol G5", 2018, new BigDecimal("35000.00"),
                List.of("http://img.com/gol.jpg"), "Carro popular econômico", 60000, "Volkswagen"
        );
        Vehicle savedVehicle = vehicleService.insert(vehicleDTO);
        this.vehicleId = savedVehicle.getId();
    }

    @AfterEach
    void tearDown() {
        saleRepository.deleteAll();
    }

    // ========== POST /sales ==========

    @Test
    @DisplayName("POST /sales - Deve criar venda e retornar 201 quando payload é válido")
    void insert_ShouldReturn201_WhenPayloadIsValid() throws Exception {
        String json = String.format("""
            {
                "client": "%s",
                "vehicle": "%s"
            }
        """, clientId, vehicleId);

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.client").value(clientId.toString()))
                .andExpect(jsonPath("$.vehicle").value(vehicleId.toString()))
                .andExpect(jsonPath("$.saleDate").exists());
    }

    @Test
    @DisplayName("POST /sales - Deve retornar 404 quando client ou vehicle não existem")
    void insert_ShouldReturn404_WhenRelatedIdsDoNotExist() throws Exception {
        String json = String.format("""
            {
                "client": "%s",
                "vehicle": "%s"
            }
        """, UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    // ========== GET /sales ==========

    @Test
    @DisplayName("GET /sales - Deve retornar lista de vendas quando existirem registros")
    void findAll_ShouldReturnList_WhenSalesExist() throws Exception {
        // Cria uma venda primeiro
        createAuxiliarySale();

        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("GET /sales - Deve retornar lista vazia quando não houver vendas")
    void findAll_ShouldReturnEmptyList_WhenNoSales() throws Exception {
        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ========== GET /sales/{id} ==========

    @Test
    @DisplayName("GET /sales/{id} - Deve retornar venda quando ID existir")
    void findById_ShouldReturnSale_WhenIdExists() throws Exception {
        MvcResult result = createAuxiliarySale();
        String body = result.getResponse().getContentAsString();
        UUID saleId = UUID.fromString(JsonPath.read(body, "$.id"));

        mockMvc.perform(get("/sales/{id}", saleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId.toString()))
                .andExpect(jsonPath("$.client").value(clientId.toString()))
                .andExpect(jsonPath("$.vehicle").value(vehicleId.toString()));
    }

    @Test
    @DisplayName("GET /sales/{id} - Deve retornar 404 quando ID não existir")
    void findById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/sales/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    // ========== PUT /sales/{id} ==========

    @Test
    @DisplayName("PUT /sales/{id} - Deve atualizar venda e retornar 200 quando válido")
    void updateById_ShouldReturn200_WhenValid() throws Exception {
        // 1. Cria a venda original
        MvcResult result = createAuxiliarySale();
        String body = result.getResponse().getContentAsString();
        UUID saleId = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Cria um novo cliente para o update
        ClientRequestDTO newClientDTO = new ClientRequestDTO();
        newClientDTO.setName("Novo Cliente");
        newClientDTO.setEmail("novo_" + UUID.randomUUID().toString().substring(0, 8) + "@teste.com");
        newClientDTO.setContact("8198" + String.format("%07d", (int)(Math.random() * 10000000)));
        newClientDTO.setCpf(String.format("%011d", (long)(Math.random() * 100000000000L)));
        Client newClient = clientService.insert(newClientDTO);

        // 3. Faz o update da venda com o novo cliente
        String updateJson = String.format("""
            {
                "client": "%s",
                "vehicle": "%s"
            }
        """, newClient.getId(), vehicleId);

        mockMvc.perform(put("/sales/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId.toString()))
                .andExpect(jsonPath("$.client").value(newClient.getId().toString()));
    }

    @Test
    @DisplayName("PUT /sales/{id} - Deve retornar 404 quando ID não existir")
    void updateById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        String json = String.format("""
            {
                "client": "%s",
                "vehicle": "%s"
            }
        """, clientId, vehicleId);

        mockMvc.perform(put("/sales/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    // ========== DELETE /sales/{id} ==========

    @Test
    @DisplayName("DELETE /sales/{id} - Deve retornar 200 ao deletar venda existente")
    void deleteById_ShouldReturn200_WhenIdExists() throws Exception {
        MvcResult result = createAuxiliarySale();
        String body = result.getResponse().getContentAsString();
        UUID saleId = UUID.fromString(JsonPath.read(body, "$.id"));

        mockMvc.perform(delete("/sales/{id}", saleId))
                .andExpect(status().isOk());

        assertFalse(saleRepository.existsById(saleId));
    }

    @Test
    @DisplayName("DELETE /sales/{id} - Deve retornar 404 quando ID não existir")
    void deleteById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/sales/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    // ========== Helper ==========

    private MvcResult createAuxiliarySale() throws Exception {
        String json = String.format("""
            {
                "client": "%s",
                "vehicle": "%s"
            }
        """, clientId, vehicleId);

        return mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
    }
}

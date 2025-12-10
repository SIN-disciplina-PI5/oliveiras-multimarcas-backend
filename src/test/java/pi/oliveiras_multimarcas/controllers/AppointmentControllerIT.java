package pi.oliveiras_multimarcas.controllers;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;
import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.services.ClientService;
import pi.oliveiras_multimarcas.services.VehicleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Desabilita filtros de segurança (JWT) para focar na lógica do controlador
@Transactional
public class AppointmentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    private UUID vehicleId;
    private UUID clientId;

    @BeforeEach
    void setUp() {
        // Prepara o banco de dados com um Veículo e um Cliente válidos antes de cada teste

        // Cria Veículo
        VehicleRequestDTO vehicleDTO = new VehicleRequestDTO(
                "Fiat Uno", 2015, new BigDecimal("25000.00"),
                List.of("http://img1.com"), "Carro econômico", 50000, "Fiat"
        );
        Vehicle savedVehicle = vehicleService.insert(vehicleDTO);
        this.vehicleId = savedVehicle.getId();

        // Cria Cliente
        ClientRequestDTO clientDTO = new ClientRequestDTO();
        clientDTO.setUsername("Maria Teste");
        clientDTO.setEmail("maria@teste.com");
        clientDTO.setPassword("123456");
        clientDTO.setContact("99999999");
        clientDTO.setRole(UserRole.USER);

        // Verifica se já existe para evitar erro de duplicidade em testes repetidos ou usa try/catch
        try {
            Client savedClient = clientService.insert(clientDTO);
            this.clientId = savedClient.getId();
        } catch (Exception e) {
            // Caso o email já exista no banco em memória de outros testes, buscamos ele
            this.clientId = clientService.findByEmail("maria@teste.com").getId();
        }
    }

    @Test
    public void shouldCreateAppointment() throws Exception {
        String json = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2025-10-20",
                "schedulingTime": "14:00:00",
                "description": "Interesse na compra",
                "status": "PENDING"
            }
        """, vehicleId, clientId);

        mockMvc.perform(
                        post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleId").value(vehicleId.toString()))
                .andExpect(jsonPath("$.clientId").value(clientId.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void shouldReturnNotFoundWhenCreatingWithInvalidRelatedIds() throws Exception {
        // Tenta criar agendamento com IDs que não existem
        String json = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2025-10-20",
                "schedulingTime": "14:00:00",
                "description": "Teste Falha"
            }
        """, UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(
                        post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindAllAppointments() throws Exception {
        // Cria um agendamento primeiro
        createAuxiliaryAppointment();

        mockMvc.perform(
                        get("/appointments")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    public void shouldFindAppointmentById() throws Exception {
        // Cria um agendamento e pega o ID retornado
        String json = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2025-11-15",
                "schedulingTime": "10:00:00",
                "description": "Visita"
            }
        """, vehicleId, clientId);

        MvcResult result = mockMvc.perform(
                post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated()).andReturn();

        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // Busca pelo ID
        mockMvc.perform(
                        get("/appointments/" + id)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void shouldReturnNotFoundWhenAppointmentIdDoesNotExist() throws Exception {
        mockMvc.perform(
                        get("/appointments/" + UUID.randomUUID())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateAppointmentStatus() throws Exception {
        // 1. Criar agendamento
        MvcResult result = createAuxiliaryAppointment();
        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Atualizar status via PUT (usando RequestParam conforme o Controller)
        mockMvc.perform(
                        put("/appointments/" + id + "/status")
                                .param("status", "SERVICED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SERVICED"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingStatusOfInvalidId() throws Exception {
        mockMvc.perform(
                        put("/appointments/" + UUID.randomUUID() + "/status")
                                .param("status", "SERVICED")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAppointmentById() throws Exception {
        // 1. Criar agendamento
        MvcResult result = createAuxiliaryAppointment();
        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Deletar
        mockMvc.perform(
                        delete("/appointments/" + id)
                )
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingInvalidId() throws Exception {
        mockMvc.perform(
                        delete("/appointments/" + UUID.randomUUID())
                )
                .andExpect(status().isNotFound());
    }

    // Método auxiliar para criar um agendamento válido durante os testes
    private MvcResult createAuxiliaryAppointment() throws Exception {
        String json = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2025-12-01",
                "schedulingTime": "09:00:00",
                "description": "Auxiliar"
            }
        """, vehicleId, clientId);

        return mockMvc.perform(
                post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();
    }
}
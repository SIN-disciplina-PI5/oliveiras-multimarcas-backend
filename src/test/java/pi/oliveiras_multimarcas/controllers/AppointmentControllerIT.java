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
import pi.oliveiras_multimarcas.dto.ClientRequestDTO;
import pi.oliveiras_multimarcas.dto.VehicleRequestDTO;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Vehicle;
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
        clientDTO.setName("Maria Teste");
        clientDTO.setEmail("maria@teste.com");
        clientDTO.setContact("99999999");
        clientDTO.setCpf("12345678928");
        clientDTO.setContact("81900001232");

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
    public void shouldReturnConflictWhenCreatingWithDuplicateSchedule() throws Exception {
        // 1. Cria um agendamento
        createAuxiliaryAppointment();

        // 2. Tenta criar outro agendamento no mesmo horário
        String json = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2027-12-01",
                "schedulingTime": "09:00:00",
                "description": "Conflito de horário"
            }
        """, vehicleId, clientId);

        mockMvc.perform(
                        post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict());
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
    public void shouldUpdateAppointmentFully() throws Exception {
        // 1. Criar agendamento
        MvcResult result = createAuxiliaryAppointment();
        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Atualizar data + hora + status via PUT /{id}
        String updateJson = """
            {
                "schedulingDate": "2027-06-15",
                "schedulingTime": "16:30:00",
                "description": "Reagendado pelo cliente",
                "status": "SERVICED"
            }
        """;

        mockMvc.perform(
                        put("/appointments/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.schedulingDate").value("2027-06-15"))
                .andExpect(jsonPath("$.schedulingTime").value("16:30:00"))
                .andExpect(jsonPath("$.description").value("Reagendado pelo cliente"))
                .andExpect(jsonPath("$.status").value("SERVICED"));
    }

    @Test
    public void shouldUpdateAppointmentWithoutChangingStatus() throws Exception {
        // 1. Criar agendamento (status padrão = PENDING)
        MvcResult result = createAuxiliaryAppointment();
        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Atualizar apenas data e hora, sem enviar status (deve manter PENDING)
        String updateJson = """
            {
                "schedulingDate": "2027-07-20",
                "schedulingTime": "11:00:00",
                "description": "Nova descrição"
            }
        """;

        mockMvc.perform(
                        put("/appointments/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedulingDate").value("2027-07-20"))
                .andExpect(jsonPath("$.schedulingTime").value("11:00:00"))
                .andExpect(jsonPath("$.description").value("Nova descrição"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void shouldReturnConflictWhenUpdatingToExistingSchedule() throws Exception {
        // 1. Criar primeiro agendamento (2027-12-01 09:00)
        createAuxiliaryAppointment();

        // 2. Criar segundo agendamento em horário diferente
        String json2 = String.format("""
            {
                "vehicleId": "%s",
                "clientId": "%s",
                "schedulingDate": "2027-12-02",
                "schedulingTime": "10:00:00",
                "description": "Segundo agendamento"
            }
        """, vehicleId, clientId);

        MvcResult result2 = mockMvc.perform(
                post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2)
        ).andExpect(status().isCreated()).andReturn();

        String body2 = result2.getResponse().getContentAsString();
        UUID id2 = UUID.fromString(JsonPath.read(body2, "$.id"));

        // 3. Tentar atualizar o segundo agendamento para o mesmo horário do primeiro
        String updateJson = """
            {
                "schedulingDate": "2027-12-01",
                "schedulingTime": "09:00:00",
                "description": "Tentativa de conflito"
            }
        """;

        mockMvc.perform(
                        put("/appointments/" + id2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldAllowUpdateToSameScheduleOfSameAppointment() throws Exception {
        // 1. Criar agendamento
        MvcResult result = createAuxiliaryAppointment();
        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        // 2. Atualizar mantendo o mesmo horário (apenas muda a descrição) — não deve dar conflito
        String updateJson = """
            {
                "schedulingDate": "2027-12-01",
                "schedulingTime": "09:00:00",
                "description": "Apenas mudei a descrição"
            }
        """;

        mockMvc.perform(
                        put("/appointments/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Apenas mudei a descrição"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentAppointment() throws Exception {
        String updateJson = """
            {
                "schedulingDate": "2027-06-15",
                "schedulingTime": "16:30:00",
                "description": "Não existe"
            }
        """;

        mockMvc.perform(
                        put("/appointments/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
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
                "schedulingDate": "2027-12-01",
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
package pi.oliveiras_multimarcas.unit;

import org.junit.jupiter.api.Test;
import pi.oliveiras_multimarcas.DTO.AppointmentRequestDTO;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppointmentTests {

    @Test
    public void shouldCreateAppointmentFromDTO() {
        // Cenário: Criar mocks/stubs das dependências
        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setModel("Civic");

        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setName("João");

        // Criar o DTO com dados de entrada
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setVehicleId(vehicle.getId());
        dto.setClientId(client.getId());
        dto.setSchedulingDate(LocalDate.of(2025, 12, 25));
        dto.setSchedulingTime(LocalTime.of(14, 30));
        dto.setDescription("Test Drive");
        dto.setStatus(Status.PENDING);

        // Ação: Instanciar a entidade usando o construtor personalizado
        Appointment appointment = new Appointment(dto, vehicle, client);

        // Verificação: Assegurar que os dados foram mapeados corretamente
        assertNotNull(appointment);
        assertEquals(vehicle, appointment.getVehicle());
        assertEquals(client, appointment.getClient());
        assertEquals(dto.getSchedulingDate(), appointment.getSchedulingDate());
        assertEquals(dto.getSchedulingTime(), appointment.getSchedulingTime());
        assertEquals(dto.getDescription(), appointment.getDescription());
        assertEquals(dto.getStatus(), appointment.getStatus());
    }

    @Test
    public void shouldSetAndGetValuesCorrectly() {
        // Teste simples de Getters e Setters para cobertura completa da entidade
        Appointment appointment = new Appointment();
        UUID id = UUID.randomUUID();
        Status status = Status.SERVICED;

        appointment.setId(id);
        appointment.setStatus(status);

        assertEquals(id, appointment.getId());
        assertEquals(status, appointment.getStatus());
    }
}
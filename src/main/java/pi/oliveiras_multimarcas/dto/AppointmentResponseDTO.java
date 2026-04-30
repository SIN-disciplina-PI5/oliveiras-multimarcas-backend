package pi.oliveiras_multimarcas.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResponseDTO {
    private UUID id;
    private UUID vehicleId;
    private String vehicleModel; // Denormalizado para conveniência
    private UUID clientId;
    private String clientName; // Denormalizado para conveniência
    private LocalDate schedulingDate;
    private LocalTime schedulingTime;
    private String description;
    private Status status;

    public AppointmentResponseDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.vehicleId = appointment.getVehicle().getId();
        this.vehicleModel = appointment.getVehicle().getModel();
        this.clientId = appointment.getClient().getId();
        this.clientName = appointment.getClient().getName();
        this.schedulingDate = appointment.getSchedulingDate();
        this.schedulingTime = appointment.getSchedulingTime();
        this.description = appointment.getDescription();
        this.status = appointment.getStatus();
    }
}
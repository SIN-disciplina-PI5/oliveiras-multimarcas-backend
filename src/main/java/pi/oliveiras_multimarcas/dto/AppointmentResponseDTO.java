package pi.oliveiras_multimarcas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResponseDTO {
    private UUID id;
    private UUID vehicleId;
    private String vehicleModel;
    private ClientResponseDTO client;
    private LocalDate schedulingDate;
    private LocalTime schedulingTime;
    private AppointmentStatus status;

    public AppointmentResponseDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.vehicleId = appointment.getVehicle().getId();
        this.vehicleModel = appointment.getVehicle().getModel();
        this.client = new ClientResponseDTO(appointment.getClient());
        this.schedulingDate = appointment.getSchedulingDate();
        this.schedulingTime = appointment.getSchedulingTime();
        this.status = appointment.getStatus();
    }
}
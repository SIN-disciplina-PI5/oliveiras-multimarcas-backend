package pi.oliveiras_multimarcas.DTO;

import lombok.Getter;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentRequestDTO {

    private UUID vehicleId;
    private UUID clientId;
    private LocalDate schedulingDate;
    private LocalTime schedulingTime;
    private String description;
    private Status status;
}

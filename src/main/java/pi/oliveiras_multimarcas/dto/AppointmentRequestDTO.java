package pi.oliveiras_multimarcas.dto; // ESTA LINHA É OBRIGATÓRIA

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentRequestDTO {

    @NotNull(message = "O ID do veículo é obrigatório")
    private UUID vehicleId;

    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID clientId;

    @NotNull(message = "A data de agendamento é obrigatória")
    @FutureOrPresent(message = "A data de agendamento deve ser no presente ou no futuro")
    private LocalDate schedulingDate;

    @NotNull(message = "O horário de agendamento é obrigatório")
    private LocalTime schedulingTime;

    private String description;
    private Status status;
}

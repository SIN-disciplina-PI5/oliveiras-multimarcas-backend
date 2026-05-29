package pi.oliveiras_multimarcas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentUpdateDTO {

    @NotNull(message = "A data de agendamento é obrigatória")
    @FutureOrPresent(message = "A data de agendamento deve ser no presente ou no futuro")
    private LocalDate schedulingDate;

    @NotNull(message = "O horário de agendamento é obrigatório")
    private LocalTime schedulingTime;

    private String description;
    private AppointmentStatus status;
}

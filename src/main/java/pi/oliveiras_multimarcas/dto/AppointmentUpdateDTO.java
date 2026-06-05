package pi.oliveiras_multimarcas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private LocalDate schedulingDate;

    @NotNull(message = "O horário é obrigatório")
    private LocalTime schedulingTime;

    private AppointmentStatus status;
    @NotNull(message = "O cliente é obrigatório")
    private ClientRequestDTO client;
}

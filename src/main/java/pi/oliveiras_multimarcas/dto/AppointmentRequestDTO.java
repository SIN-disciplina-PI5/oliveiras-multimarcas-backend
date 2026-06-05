package pi.oliveiras_multimarcas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDTO {

    @NotNull(message = "O ID do veículo é obrigatório")
    private UUID vehicleId;

    @NotNull(message = "O ID do cliente é obrigatório")
    private ClientRequestDTO client;

    @NotNull(message = "A data é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent(message = "A data de agendamento deve ser no presente ou no futuro")
    private LocalDate schedulingDate;

    @NotNull(message = "O horário é obrigatório")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime schedulingTime;

    private AppointmentStatus status;
}
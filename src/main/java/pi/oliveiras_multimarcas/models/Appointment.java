package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.DTO.AppointmentRequestDTO;
import pi.oliveiras_multimarcas.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @Column(name = "scheduling_date", nullable = false)
    private LocalDate schedulingDate;

    @NotNull
    @Column(name = "scheduling_time", nullable = false)
    private LocalTime schedulingTime;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Construtor via DTO
    public Appointment(AppointmentRequestDTO dto, Vehicle vehicle, Client client) {
        this.vehicle = vehicle;
        this.client = client;
        this.schedulingDate = dto.getSchedulingDate();
        this.schedulingTime = dto.getSchedulingTime();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
    }
}

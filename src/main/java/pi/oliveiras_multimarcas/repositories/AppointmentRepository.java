package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // Verifica conflito de horário (exclui o próprio agendamento para cenário de update)
    boolean existsBySchedulingDateAndSchedulingTimeAndIdNot(LocalDate schedulingDate, LocalTime schedulingTime, UUID id);

    // Verifica conflito de horário (para cenário de criação)
    boolean existsBySchedulingDateAndSchedulingTime(LocalDate schedulingDate, LocalTime schedulingTime);
}
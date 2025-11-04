package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.AppointmentRequestDTO;
import pi.oliveiras_multimarcas.DTO.AppointmentResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.enums.Status;
import pi.oliveiras_multimarcas.repositories.AppointmentRepository;
import pi.oliveiras_multimarcas.repositories.ClientRepository;
import pi.oliveiras_multimarcas.repositories.VehicleRepositorie;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VehicleRepositorie vehicleRepository; // Para buscar o veículo

    @Autowired
    private ClientRepository clientRepository; // Para buscar o cliente


    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) { //
        // 1. Busca as entidades relacionadas
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new NoSuchException("Veículo")); //

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NoSuchException("Cliente")); //

        // 2. Cria a entidade Appointment
        Appointment appointment = new Appointment(dto, vehicle, client); //

        // 3. Define um status padrão (PENDING) se nenhum for enviado
        if (dto.getStatus() == null) {
            appointment.setStatus(Status.PENDING); //
        }

        // 4. Salva a entidade no banco
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 5. Retorna o DTO de Resposta
        return new AppointmentResponseDTO(savedAppointment);
    }

    @Transactional(readOnly = true)
    public AppointmentResponseDTO findById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Agendamento")); //
        return new AppointmentResponseDTO(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findAll() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponseDTO::new) // Converte cada Appointment em AppointmentResponseDTO
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponseDTO updateStatus(UUID id, Status status) { //
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Agendamento")); //

        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return new AppointmentResponseDTO(updatedAppointment);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new NoSuchException("Agendamento"); //
        }
        appointmentRepository.deleteById(id);
    }
}
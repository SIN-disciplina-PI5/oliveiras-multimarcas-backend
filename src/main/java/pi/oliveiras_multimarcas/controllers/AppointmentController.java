package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.AppointmentRequestDTO;
import pi.oliveiras_multimarcas.DTO.AppointmentResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.enums.Status;
import pi.oliveiras_multimarcas.services.AppointmentService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AppointmentRequestDTO dto) {
        try {
            AppointmentResponseDTO newAppointment = appointmentService.createAppointment(dto);
            // Retorna 201 Created com a localização do novo recurso
            URI location = URI.create("/appointments/" + newAppointment.getId());
            return ResponseEntity.created(location).body(newAppointment);
        } catch (NoSuchException e) {
            // Caso o Cliente ou Veículo não sejam encontrados
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
        List<AppointmentResponseDTO> appointments = appointmentService.findAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        try {
            AppointmentResponseDTO appointment = appointmentService.findById(id);
            return ResponseEntity.ok(appointment);
        } catch (NoSuchException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable UUID id,
            @RequestParam Status status) { //
        try {
            AppointmentResponseDTO updatedAppointment = appointmentService.updateStatus(id, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        try {
            appointmentService.deleteById(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Retorna 404 Not Found
        }
    }
}
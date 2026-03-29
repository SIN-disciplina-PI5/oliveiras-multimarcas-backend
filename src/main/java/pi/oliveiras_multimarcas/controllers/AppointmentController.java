package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.AppointmentRequestDTO;
import pi.oliveiras_multimarcas.DTO.AppointmentResponseDTO;
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
        AppointmentResponseDTO newAppointment = appointmentService.createAppointment(dto);
        // Retorna 201 Created com a localização do novo recurso
        URI location = URI.create("/appointments/" + newAppointment.getId());
        return ResponseEntity.created(location).body(newAppointment);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
        List<AppointmentResponseDTO> appointments = appointmentService.findAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO appointment = appointmentService.findById(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable UUID id,
            @RequestParam Status status) { //
        AppointmentResponseDTO updatedAppointment = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.DTO.VehicleResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.services.VehicleService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class VehicleControllers {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/")
    public ResponseEntity<List<VehicleResponseDTO>> findAll() {
        List<Vehicle> vehicles = vehicleService.findAll();
        List<VehicleResponseDTO> vehiclesResponseDTO = vehicles.stream()
                .map(VehicleResponseDTO::new)
                .toList();

        return ResponseEntity.ok().body(vehiclesResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> findById(@PathVariable UUID id) {
        Vehicle vehicle;
        try {
            vehicle = vehicleService.findyById(id);
        } catch (NoSuchException e) {
            return ResponseEntity.notFound().build();
        }

        VehicleResponseDTO vehicleResponseDTO = new VehicleResponseDTO(vehicle);

        return ResponseEntity.ok().body(vehicleResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleResponseDTO> insert(@RequestBody VehicleRequestDTO dto, UriComponentsBuilder uriBuilder) {

        Vehicle vehicle = vehicleService.insert(dto);
        VehicleResponseDTO vehicleResponseDTO = new VehicleResponseDTO(vehicle);

        URI uri = uriBuilder
                .path("/vehicles/{id}")
                .buildAndExpand(vehicleResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(vehicleResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByID(@PathVariable UUID id) {
        try {
            vehicleService.delete(id);
        } catch (NoSuchException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateById(@PathVariable UUID id, @RequestBody VehicleRequestDTO dto) {
        Vehicle vehicle;

        try{
            vehicle = vehicleService.update(dto, id);
        } catch (NoSuchException e) {
            return ResponseEntity.notFound().build();
        }

        VehicleResponseDTO vehicleResponseDTO = new VehicleResponseDTO(vehicle);

        return ResponseEntity.ok().body(vehicleResponseDTO);
    }
}


package pi.oliveiras_multimarcas.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;
import pi.oliveiras_multimarcas.DTO.ClientResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.services.ClientService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> findAll() {
        List<Client> clients = clientService.findAll();
        List<ClientResponseDTO> clientResponseDTOS = clients.stream().map(ClientResponseDTO::new).toList();
        return ResponseEntity.ok(clientResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable UUID id) {
        try {
            Client client = clientService.findById(id);
            ClientResponseDTO clientResponse = new ClientResponseDTO(client);
            return ResponseEntity.ok().body(clientResponse);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> insert(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        try {
            if (clientRequestDTO.getUsername() == null || clientRequestDTO.getEmail() == null || clientRequestDTO.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }
            Client client = clientService.insert(clientRequestDTO);
            ClientResponseDTO clientResponseDTO = new ClientResponseDTO(client);
            return ResponseEntity.status(201).body(clientResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody @Valid ClientRequestDTO ClientRequestDTO) {
        try {
            Client client = clientService.updateById(id, ClientRequestDTO);
            ClientResponseDTO clientResponseDTO = new ClientResponseDTO(client);
            return ResponseEntity.ok(clientResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        try {
            clientService.deleteById(id);
            return ResponseEntity.ok().body("Usuário deletado");
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
}

package pi.oliveiras_multimarcas.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.dto.ProposalRequestDTO;
import pi.oliveiras_multimarcas.dto.ProposalResponseDTO;
import pi.oliveiras_multimarcas.models.Proposal;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;
import pi.oliveiras_multimarcas.services.ProposalService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDTO> create(@Valid @RequestBody ProposalRequestDTO dto) {
        Proposal proposal = proposalService.create(dto);
        return ResponseEntity.status(201).body(new ProposalResponseDTO(proposal));
    }

    @GetMapping
    public ResponseEntity<List<ProposalResponseDTO>> findAll() {
        List<ProposalResponseDTO> list = proposalService.findAll().stream()
                .map(ProposalResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ProposalResponseDTO(proposalService.findById(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProposalResponseDTO>> findByStatus(@PathVariable ProposalStatus status) {
        List<ProposalResponseDTO> list = proposalService.findByStatus(status).stream()
                .map(ProposalResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProposalResponseDTO>> findByClient(@PathVariable UUID clientId) {
        List<ProposalResponseDTO> list = proposalService.findByClient(clientId).stream()
                .map(ProposalResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ProposalResponseDTO> accept(@PathVariable UUID id) {
        Proposal proposal = proposalService.accept(id);
        return ResponseEntity.ok(new ProposalResponseDTO(proposal));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ProposalResponseDTO> reject(@PathVariable UUID id) {
        Proposal proposal = proposalService.reject(id);
        return ResponseEntity.ok(new ProposalResponseDTO(proposal));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ProposalResponseDTO> cancel(@PathVariable UUID id) {
        Proposal proposal = proposalService.cancel(id);
        return ResponseEntity.ok(new ProposalResponseDTO(proposal));
    }
}
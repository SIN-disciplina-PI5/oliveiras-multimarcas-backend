package pi.oliveiras_multimarcas.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.SaleRequestDTO;
import pi.oliveiras_multimarcas.DTO.SaleResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.EntityNotFoundException;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.services.SaleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
public class SaleControllers {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        List<Sale> employees = saleService.findAll();
        List<SaleResponseDTO> saleResponseDTOS = employees.stream().map(SaleResponseDTO::new).toList();
        return ResponseEntity.ok(saleResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable UUID id) {
        Sale sale = saleService.findById(id);
        SaleResponseDTO saleResponse = new SaleResponseDTO(sale);

        return ResponseEntity.ok().body(saleResponse);
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> insert(@RequestBody SaleRequestDTO dto) {
        Sale sale = saleService.insert(dto);
        SaleResponseDTO saleResponse = new SaleResponseDTO(sale);
        return ResponseEntity.status(201).body(saleResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody SaleRequestDTO dto) {
        Sale sale = saleService.updateById(id, dto);
        SaleResponseDTO saleResponse = new SaleResponseDTO(sale);

        return ResponseEntity.ok(saleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        saleService.deleteById(id);
        return ResponseEntity.ok().body("Usuário deletado");
    }
}
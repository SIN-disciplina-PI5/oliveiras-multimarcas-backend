package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.SaleRequestDTO;
import pi.oliveiras_multimarcas.DTO.SaleResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.services.SaleService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        try {
            Sale sale = saleService.findById(id);
            SaleResponseDTO saleResponse = new SaleResponseDTO(sale);

            return ResponseEntity.ok().body(saleResponse);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> insert(@RequestBody SaleRequestDTO dto) {
        try {
            Sale sale = saleService.insert(dto);
            SaleResponseDTO saleResponse = new SaleResponseDTO(sale);
            return ResponseEntity.status(201).body(saleResponse);
        } catch (InvalidArguments e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody SaleRequestDTO dto) {
        try {
            Sale sale = saleService.updateById(id, dto);
            SaleResponseDTO saleResponse = new SaleResponseDTO(sale);

            return ResponseEntity.ok(saleResponse);
        } catch (InvalidArguments e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        try {
            saleService.deleteById(id);
            return ResponseEntity.ok().body("Usuário deletado");
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
}

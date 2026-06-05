package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.dto.PreferencesRequestDTO;
import pi.oliveiras_multimarcas.dto.PreferensesResponseDTO;
import pi.oliveiras_multimarcas.services.PreferencesService;

@RestController
@RequestMapping("/preferences")
public class PreferencesControllers {

    @Autowired
    private PreferencesService preferencesService;

    @GetMapping
    public ResponseEntity<PreferensesResponseDTO> getPreferences(){
        return ResponseEntity.ok().body(preferencesService.getPreferences());
    }

    @PutMapping
    public ResponseEntity<?> updatePreferences(@RequestBody PreferencesRequestDTO dto){
        return ResponseEntity.ok().body(preferencesService.updatePreferences(dto));
    }
}

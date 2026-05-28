package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.dto.DashboardResponseDTO;
import pi.oliveiras_multimarcas.services.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboardData(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        
        DashboardResponseDTO response = dashboardService.getDashboardData(start, end);
        return ResponseEntity.ok(response);
    }
}
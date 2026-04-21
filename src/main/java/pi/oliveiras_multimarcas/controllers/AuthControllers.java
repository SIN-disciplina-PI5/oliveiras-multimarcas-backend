package pi.oliveiras_multimarcas.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.*;
import pi.oliveiras_multimarcas.exceptions.EntityNotFoundException;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.ClientService;
import pi.oliveiras_multimarcas.services.EmployeeService;
import pi.oliveiras_multimarcas.services.RecaptchaService;
import pi.oliveiras_multimarcas.services.TokenService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody EmployeeRequestDTO dto) {
        employeeService.insert(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequestDTO dto){

        Map<String, Object> response = recaptchaService.verify(dto.getRecaptchaToken());

        if (response == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Boolean success = (Boolean) response.get("success");

        if (!Boolean.TRUE.equals(success)) {
            System.out.println("Erro recaptcha: " + response);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Double score = response.get("score") != null
                ? ((Number) response.get("score")).doubleValue()
                : 0.0;

        if (score < 0.6) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Employee employee = employeeService.findByEmail(dto.getEmail());
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = jwtUtil.generateTokenRefresh(employee.getId(), employee.getEmail());
        String accessToken = jwtUtil.generateTokenAcess(employee.getId(), employee.getEmail());

        tokenService.insert(refreshToken, employee.getId());

        SignInResponseDTO responseDTO = new SignInResponseDTO(refreshToken, accessToken);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout( HttpServletRequest authorization){
        String authHeader = authorization.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);

        tokenService.deleteByToken(token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken( HttpServletRequest authorization) {
        String authHeader = authorization.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);

        boolean isTokenValid = jwtUtil.isTokenValid(token, "refresh");
        if (!isTokenValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean isTokenActive = tokenService.isTokenActive(token);
        if (!isTokenActive) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Map<String, Object> claim = jwtUtil.extractClaims(token, "refresh");
        Object userId = claim.get("id");
        UUID id = UUID.fromString(userId.toString());

        Employee employee = employeeService.findById(id);
        RefreshTokenResponseDTO acessToken = new RefreshTokenResponseDTO(jwtUtil.generateTokenAcess(id, employee.getEmail()));
        return ResponseEntity.ok().body(acessToken);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(){

        return  ResponseEntity.ok().build();
    }
}
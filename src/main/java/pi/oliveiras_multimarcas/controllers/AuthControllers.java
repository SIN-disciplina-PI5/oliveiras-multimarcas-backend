package pi.oliveiras_multimarcas.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.*;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.ClientService;
import pi.oliveiras_multimarcas.services.EmployeeService;
import pi.oliveiras_multimarcas.services.TokenService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

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
    public ResponseEntity<SignInResponseDTO> signin(@Valid @RequestBody SignInRequestDTO dto){

        Employee employee;
        Client client;
        
        // Tenta logar primeiro como Funcionário
        employee = employeeService.findByEmail(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = jwtUtil.generateTokenRefresh(employee.getId(), employee.getEmail());
        String acessToken = jwtUtil.generateTokenAcess(employee.getId(), employee.getEmail());
        tokenService.insert(refreshToken, employee.getId());
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO(refreshToken,acessToken);

        return ResponseEntity.ok().body(signInResponseDTO);
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
}
package pi.oliveiras_multimarcas.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.*;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
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
        try {
            // Agora usamos o employeeService diretamente
            employeeService.insert(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signin(@RequestBody SignInRequestDTO dto){

        Employee employee;
        Client client;
        
        // Tenta logar primeiro como Funcionário
        try{
            employee = employeeService.findByEmail(dto.getEmail());
            if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String refreshToken = jwtUtil.generateTokenRefresh(employee.getId(), employee.getEmail());
            String acessToken = jwtUtil.generateTokenAcess(employee.getId(), employee.getEmail());
            tokenService.insert(refreshToken);
            SignInResponseDTO signInResponseDTO = new SignInResponseDTO(refreshToken,acessToken);

            return ResponseEntity.ok().body(signInResponseDTO);
        
        } catch (NoSuchException e) {
            // Se não achar funcionário, tenta como Cliente
            try{
                client = clientService.findByEmail(dto.getEmail());
                if (!passwordEncoder.matches(dto.getPassword(), client.getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                String refreshToken = jwtUtil.generateTokenRefresh(client.getId(), client.getEmail());
                String acessToken = jwtUtil.generateTokenAcess(client.getId(), client.getEmail());
                tokenService.insert(refreshToken);
                SignInResponseDTO signInResponseDTO = new SignInResponseDTO(refreshToken,acessToken);

                return ResponseEntity.ok().body(signInResponseDTO);
            } catch (NoSuchException j) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader HttpServletRequest authorization){
        String authHeader = authorization.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);

        try{
            tokenService.deleteByToken(token);
        } catch (NoSuchException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestHeader HttpServletRequest authorization) {
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

        try{
            Employee employee = employeeService.findById(id);
            RefreshTokenResponseDTO acessToken = new RefreshTokenResponseDTO(jwtUtil.generateTokenAcess(id, employee.getEmail()));
            return ResponseEntity.ok().body(acessToken);
        } catch (NoSuchException e){
            try{
                Client client = clientService.findById(id);
                RefreshTokenResponseDTO acessToken = new RefreshTokenResponseDTO(jwtUtil.generateTokenAcess(id, client.getEmail()));
                return ResponseEntity.ok().body(acessToken);
            }catch (NoSuchException j){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }
}
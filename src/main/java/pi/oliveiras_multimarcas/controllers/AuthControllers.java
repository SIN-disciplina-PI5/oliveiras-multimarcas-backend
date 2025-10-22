package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.RefreshTokenResponseDTO;
import pi.oliveiras_multimarcas.DTO.SignInRequestDTO;
import pi.oliveiras_multimarcas.DTO.SignInResponseDTO;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.TokenService;
import pi.oliveiras_multimarcas.services.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<Void> sigup(@RequestBody UserRequestDTO dto){

        try {
            userService.insert(dto);
        } catch (Exception e) {
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> sigin(@RequestBody SignInRequestDTO dto){

        User user;
        try{
            user = userService.findByEmail(dto.getEmail());
        } catch (NoSuchException e) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String refreshToken = jwtUtil.generateTokenRefresh(user.getId());
        String acessToken = jwtUtil.generateTokenAcess(user.getId());
        tokenService.insert(refreshToken);
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO(refreshToken,acessToken);

        return ResponseEntity.ok().body(signInResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader String token){
        try{
            tokenService.deleteByToken(token);
        } catch (NoSuchException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestHeader String token){

        boolean isTokenValid = jwtUtil.isTokenValid(token, "refresh");
        if (!isTokenValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean isTokenActive = tokenService.isTokenActive(token);
        if (!isTokenActive) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Map<String, Object> claim = jwtUtil.extractClaims(token, "refresh");
        Object userId = claim.get("id");
        UUID id = UUID.fromString(userId.toString());

        RefreshTokenResponseDTO acessToken = new RefreshTokenResponseDTO(jwtUtil.generateTokenAcess(id));

        return ResponseEntity.ok().body(acessToken);
    }
}

package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pi.oliveiras_multimarcas.DTO.SignInRequestDTO;
import pi.oliveiras_multimarcas.DTO.SignInResponseDTO;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/sigup")
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

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String refreshToken = jwtUtil.generateTokenRefresh(user.getId());
        String acessToken = jwtUtil.generateTokenAcess(user.getId());

        SignInResponseDTO signInResponseDTO = new SignInResponseDTO(refreshToken,acessToken);

        return ResponseEntity.ok().body(signInResponseDTO);
    }
}

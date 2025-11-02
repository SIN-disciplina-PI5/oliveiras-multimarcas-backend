package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.DTO.UserResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOS = users.stream().map(user -> {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return userResponseDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO > findById(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return ResponseEntity.ok().body(userResponseDTO);
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            if (userRequestDTO.getName() == null || userRequestDTO.getEmail() == null || userRequestDTO.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }
            User user = userService.insert(userResponseDTO);
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return ResponseEntity.status(201).body(userResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO user = userService.update(id, userRequestDTO);
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setPosition(user.getPosition());
            return ResponseEntity.ok(userResponseDTO);
        } catch (InvalidArguments e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().body("Usuário deletado");
        } catch (NoSuchException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
}

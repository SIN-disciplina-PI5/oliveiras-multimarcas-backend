package pi.oliveiras_multimarcas.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClientRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveValidarDTOValido() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setUsername("user123");
        dto.setEmail("teste@teste.com");
        dto.setPassword("senha123");
        dto.setContact("81999999999");
        dto.setRole(null);

        dto.setRole(pi.oliveiras_multimarcas.models.enums.UserRole.USER);

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "DTO deveria ser válido");
    }

    @Test
    void deveFalharQuandoUsernameVazio() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setUsername(""); 
        dto.setEmail("teste@teste.com");
        dto.setPassword("senha123");

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username") &&
                        v.getMessage().contains("não pode estar em branco")));
    }

    @Test
    void deveFalharQuandoEmailInvalido() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setUsername("user123");
        dto.setEmail("emailinvalido"); // inválido
        dto.setPassword("senha123");

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                        v.getMessage().contains("Formato de email inválido")));
    }

    @Test
    void deveFalharQuandoSenhaPequena() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setUsername("user123");
        dto.setEmail("teste@teste.com");
        dto.setPassword("123"); 

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("entre 5 e 20")));
    }
}

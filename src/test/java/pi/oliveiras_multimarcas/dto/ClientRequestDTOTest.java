package pi.oliveiras_multimarcas.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        dto.setName("user123");
        dto.setEmail("teste@teste.com");
        dto.setContact("81999999999");
        dto.setCpf("12345678903");

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "DTO deveria ser válido");
    }

    @Test
    void deveFalharQuandoUsernameVazio() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setEmail("teste@teste.com");

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("não pode estar em branco")));
    }

    @Test
    void deveFalharQuandoEmailInvalido() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setName("user123");
        dto.setEmail("emailinvalido"); // inválido

        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                        v.getMessage().contains("Formato de email inválido")));
    }
}

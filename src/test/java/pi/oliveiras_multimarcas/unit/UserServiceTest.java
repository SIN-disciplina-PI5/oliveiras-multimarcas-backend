package pi.oliveiras_multimarcas.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.UserRepositorie;
import pi.oliveiras_multimarcas.services.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositorie userRepositorie;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        User user = new User("Teste", "teste@email.com", "123", UserRole.USER);
        user.setId(id);

        when(userRepositorie.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findById(id);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testInsertUserSuccess() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Novo User");
        dto.setEmail("novo@email.com");
        dto.setPassword("123");

        User user = new User();
        user.setId(UUID.randomUUID());

        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(userRepositorie.save(any(User.class))).thenReturn(user);

        User created = userService.insert(dto);

        assertNotNull(created);
        verify(userRepositorie, times(1)).save(any(User.class));
    }
}

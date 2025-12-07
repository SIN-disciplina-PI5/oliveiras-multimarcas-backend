package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pi.oliveiras_multimarcas.DTO.SignInRequestDTO;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.TokenService;
import pi.oliveiras_multimarcas.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthControllers authControllers;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authControllers).build();
    }

    @Test
    void testLoginEndpointExists() throws Exception {
        SignInRequestDTO login = new SignInRequestDTO();
        login.setEmail("teste@email.com");
        login.setPassword("123");

        User mockUser = new User();
        mockUser.setEmail("teste@email.com");
        mockUser.setPassword("senha_encriptada");
        mockUser.setRole(UserRole.USER);

        // Mocks configurados
        lenient().when(userService.findByEmail(anyString())).thenReturn(mockUser);
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        lenient().when(tokenService.generateToken(any())).thenReturn("token_valido_mock");

        // Mock do JwtUtil
        lenient().when(jwtUtil.generateTokenRefresh(any(), anyString())).thenReturn("refresh_token_mock");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(login)))
                .andExpect(status().isOk());
    }
}

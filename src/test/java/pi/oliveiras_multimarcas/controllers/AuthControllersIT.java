package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;
import pi.oliveiras_multimarcas.repositories.TokenRepositorie;
import pi.oliveiras_multimarcas.security.JwtUtil;
import pi.oliveiras_multimarcas.services.EmployeeService;
import pi.oliveiras_multimarcas.services.RecaptchaService;
import pi.oliveiras_multimarcas.services.TokenService;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllersIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRepositorie tokenRepositorie;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock do RecaptchaService para evitar chamadas externas à API do Google
    @MockBean
    private RecaptchaService recaptchaService;

    private Employee testEmployee;
    private final String testPassword = "SenhaSegura123";
    private final String testEmail = "auth_test@empresa.com";

    @BeforeEach
    void setUp() {
        tokenRepositorie.deleteAll();
        employeeRepository.deleteAll();

        // Cria um employee para os testes de autenticação
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("Auth Tester");
        dto.setEmail(testEmail);
        dto.setPassword(testPassword);
        dto.setContact("81900009999");
        dto.setCpf("99988877766");
        dto.setPosition("Gerente");
        dto.setRole(UserRole.ADMIN);
        testEmployee = employeeService.insert(dto);
    }

    @AfterEach
    void tearDown() {
        tokenRepositorie.deleteAll();
        employeeRepository.deleteAll();
    }

    // ========== POST /auth/signup ==========

    @Test
    @DisplayName("POST /auth/signup - Deve registrar um novo funcionário e retornar 200")
    void signup_ShouldReturn200_WhenPayloadIsValid() throws Exception {
        String json = """
            {
                "name": "Novo Funcionario",
                "email": "novo_signup@empresa.com",
                "password": "Senha12345",
                "contact": "81911112222",
                "cpf": "11122233344",
                "position": "Vendedor",
                "role": "ADMIN"
            }
        """;

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    // ========== POST /auth/signin ==========

    @Test
    @DisplayName("POST /auth/signin - Deve retornar tokens quando credenciais e reCAPTCHA são válidos")
    void signin_ShouldReturnTokens_WhenCredentialsAreValid() throws Exception {
        // Configura o mock do RecaptchaService para retornar sucesso com score alto
        org.mockito.Mockito.when(recaptchaService.verify(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Map.of("success", true, "score", 0.9));

        String json = String.format("""
            {
                "email": "%s",
                "password": "%s",
                "recaptchaToken": "mock-token-valid"
            }
        """, testEmail, testPassword);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.acessToken").exists());
    }

    @Test
    @DisplayName("POST /auth/signin - Deve retornar 401 quando senha está incorreta")
    void signin_ShouldReturn401_WhenPasswordIsWrong() throws Exception {
        org.mockito.Mockito.when(recaptchaService.verify(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Map.of("success", true, "score", 0.9));

        String json = String.format("""
            {
                "email": "%s",
                "password": "SenhaErrada999",
                "recaptchaToken": "mock-token"
            }
        """, testEmail);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/signin - Deve retornar 403 quando reCAPTCHA falha")
    void signin_ShouldReturn403_WhenRecaptchaFails() throws Exception {
        org.mockito.Mockito.when(recaptchaService.verify(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Map.of("success", false));

        String json = String.format("""
            {
                "email": "%s",
                "password": "%s",
                "recaptchaToken": "mock-token-invalid"
            }
        """, testEmail, testPassword);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /auth/signin - Deve retornar 403 quando score do reCAPTCHA é baixo")
    void signin_ShouldReturn403_WhenRecaptchaScoreIsLow() throws Exception {
        org.mockito.Mockito.when(recaptchaService.verify(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Map.of("success", true, "score", 0.3));

        String json = String.format("""
            {
                "email": "%s",
                "password": "%s",
                "recaptchaToken": "mock-token-low-score"
            }
        """, testEmail, testPassword);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    // ========== POST /auth/logout ==========

    @Test
    @DisplayName("POST /auth/logout - Deve retornar 200 e invalidar o refresh token")
    void logout_ShouldReturn200_WhenTokenIsValid() throws Exception {
        // Gera um refresh token real e salva no banco
        String refreshToken = jwtUtil.generateTokenRefresh(testEmployee.getId(), testEmployee.getEmail());
        tokenService.insert(refreshToken, testEmployee.getId());

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /auth/logout - Deve retornar 400 quando header Authorization está ausente")
    void logout_ShouldReturn400_WhenNoAuthorizationHeader() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isBadRequest());
    }

    // ========== POST /auth/refresh ==========

    @Test
    @DisplayName("POST /auth/refresh - Deve retornar novo access token quando refresh token é válido")
    void refresh_ShouldReturnNewAccessToken_WhenRefreshTokenIsValid() throws Exception {
        // Gera um refresh token real e salva no banco
        String refreshToken = jwtUtil.generateTokenRefresh(testEmployee.getId(), testEmployee.getEmail());
        tokenService.insert(refreshToken, testEmployee.getId());

        mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acessToken").exists());
    }

    @Test
    @DisplayName("POST /auth/refresh - Deve retornar 401 quando header Authorization está ausente")
    void refresh_ShouldReturn401_WhenNoAuthorizationHeader() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/refresh - Deve retornar 401 quando refresh token é inválido")
    void refresh_ShouldReturn401_WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer token-invalido-123"))
                .andExpect(status().isUnauthorized());
    }

    // ========== POST /auth/validate ==========

    @Test
    @DisplayName("POST /auth/validate - Deve retornar 200 (endpoint de validação)")
    void validate_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/auth/validate"))
                .andExpect(status().isOk());
    }
}

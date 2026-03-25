package pi.oliveiras_multimarcas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions; // Importação necessária
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;
import pi.oliveiras_multimarcas.services.EmployeeService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Desabilita o filtro JWT para testes do controller
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Limpa o repositório de funcionários antes e depois de cada teste para garantir isolamento
    @BeforeEach
    @AfterEach
    void cleanUp() {
        employeeRepository.deleteAll();
    }

    /**
     * Helper para criar um funcionário via serviço e popular o banco de dados.
     * @return O ID do funcionário salvo.
     */
    private UUID createAndSaveEmployee() {
        pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO dto = new pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO();
        dto.setName("Funcionario Aux");
        dto.setEmail("aux@empresa.com");
        dto.setPassword("senha_segura_123");
        dto.setContact("81999999999");
        dto.setPosition("Vendedor");
        dto.setRole(UserRole.ADMIN);

        Employee saved = employeeService.insert(dto);
        return saved.getId();
    }

    /**
     * Helper para realizar um POST na rota e retornar o ResultActions (para o encadeamento de andExpect).
     */
    private ResultActions postEmployee(String email, String name, String password) throws Exception {
        String json = String.format("""
            {
                "name": "%s",
                "email": "%s",
                "password": "%s",
                "contact": "81900000000",
                "position": "Estoquista",
                "role": "ADMIN" 
            }
        """, name, email, password);

        // Retorna ResultActions para permitir o encadeamento de .andExpect()
        return mockMvc.perform(
                post("/employes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );
    }


    @Test
    @DisplayName("GET /employes - Deve retornar lista de funcionários")
    void findAll_ShouldReturnList() throws Exception {
        // Setup: cria um funcionário
        UUID id = createAndSaveEmployee();

        mockMvc.perform(get("/employes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    @DisplayName("GET /employes/{id} - Deve retornar funcionário quando ID existir")
    void findById_ShouldReturnEmployee_WhenIdExists() throws Exception {
        UUID id = createAndSaveEmployee();

        mockMvc.perform(get("/employes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Funcionario Aux"));
    }

    @Test
    @DisplayName("GET /employes/{id} - Deve retornar 404 quando ID não existir")
    void findById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/employes/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /employes - Deve retornar 201 para payload válido e verificar criptografia")
    void insert_ShouldReturn201_WhenPayloadIsValid() throws Exception {
        String email = "joao.silva@empresa.com";
        String password = "SenhaSegura123";

        MvcResult result = postEmployee(email, "João da Silva", password)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João da Silva"))
                .andExpect(jsonPath("$.email").value(email))
                .andReturn(); // Agora .andReturn() é chamado no final do encadeamento

        // Verifica se a senha foi codificada e o registro existe
        Employee saved = employeeRepository.findByEmail(email).orElseThrow();
        assertNotEquals(password, saved.getPassword(), "A senha deve estar criptografada");
    }

    @Test
    @DisplayName("POST /employes - Deve retornar 400 se faltar nome, email ou password (checa no controller)")
    void insert_ShouldReturn400_WhenRequiredFieldsAreNull() throws Exception {
        // name, email, password serão null
        String jsonWithNullFields = "{}";

        mockMvc.perform(post("/employes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithNullFields))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /employes/{id} - Deve retornar 200 e atualizar funcionário")
    void updateById_ShouldReturn200_WhenValid() throws Exception {
        // 1. Cria o funcionário inicial e obtém o ID
        MvcResult initialResult = postEmployee("inicial@empresa.com", "Nome Inicial", "senhaInicial123")
                .andExpect(status().isCreated())
                .andReturn();
        UUID id = UUID.fromString(JsonPath.read(initialResult.getResponse().getContentAsString(), "$.id"));

        String updatedEmail = "atualizado@empresa.com";
        String newPassword = "NovaSenha123";
        String updateJson = String.format("""
            {
                "name": "Funcionario Atualizado",
                "email": "%s",
                "password": "%s",
                "contact": "81900000000",
                "position": "Gerente Atualizado",
                "role": "ADMIN" 
            }
        """, updatedEmail, newPassword);

        mockMvc.perform(put("/employes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Funcionario Atualizado"))
                .andExpect(jsonPath("$.position").value("Gerente Atualizado"))
                .andExpect(jsonPath("$.email").value(updatedEmail));

        // Verifica se o email foi alterado no banco de dados e a senha foi codificada
        Employee updated = employeeRepository.findById(id).orElseThrow();
        assertNotEquals(newPassword, updated.getPassword(), "A nova senha deve estar codificada");
    }

    @Test
    @DisplayName("PUT /employes/{id} - Deve retornar 404 quando ID não existir no update")
    void updateById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        String updateJson = """
            {
                "name": "Funcionario Teste",
                "email": "teste@update.com",
                "password": "Senha123",
                "contact": "81900000000",
                "position": "Gerente",
                "role": "ADMIN" 
            }
        """;

        mockMvc.perform(put("/employes/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));
    }

    @Test
    @DisplayName("DELETE /employes/{id} - Deve retornar 200 ao deletar funcionário existente")
    void deleteById_ShouldReturn200_WhenIdExists() throws Exception {
        // 1. Cria o funcionário inicial e obtém o ID
        MvcResult initialResult = postEmployee("para_deletar@empresa.com", "Para Deletar", "delete123")
                .andExpect(status().isCreated())
                .andReturn();
        UUID id = UUID.fromString(JsonPath.read(initialResult.getResponse().getContentAsString(), "$.id"));

        // 2. Deleta o recurso
        mockMvc.perform(delete("/employes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado"));

        // 3. Verifica se foi realmente deletado
        assertFalse(employeeRepository.existsById(id));
    }

    @Test
    @DisplayName("DELETE /employes/{id} - Deve retornar 404 se o ID não existir")
    void deleteById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/employes/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));
    }
}
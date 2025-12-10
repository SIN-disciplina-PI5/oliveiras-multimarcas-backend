package pi.oliveiras_multimarcas.unit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole;

class EmployeeTests {

    @Test
    void testConstructorWithDTO() {
        // Arrange
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("Maria");
        dto.setEmail("maria@email.com");
        dto.setPassword("123456");
        dto.setContact("81999999999");
        dto.setPosition("Atendente");

        // Act
        Employee employee = new Employee(dto);

        // Assert
        assertEquals("Maria", employee.getName());
        assertEquals("maria@email.com", employee.getEmail());
        assertEquals("123456", employee.getPassword());
        assertEquals("81999999999", employee.getContact());
        assertEquals("Atendente", employee.getPosition());
        assertEquals(UserRole.ADMIN, employee.getRole());  // definido no construtor
    }

    @Test
    void testNoArgsConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
    }

    @Test
    void testSettersAndGetters() {
        Employee employee = new Employee();

        employee.setName("João");
        employee.setEmail("joao@email.com");
        employee.setPassword("senha123");
        employee.setRole(UserRole.ADMIN);
        employee.setContact("81988887777");
        employee.setPosition("Gerente");

        assertEquals("João", employee.getName());
        assertEquals("joao@email.com", employee.getEmail());
        assertEquals("senha123", employee.getPassword());
        assertEquals(UserRole.ADMIN, employee.getRole());
        assertEquals("81988887777", employee.getContact());
        assertEquals("Gerente", employee.getPosition());
    }
}

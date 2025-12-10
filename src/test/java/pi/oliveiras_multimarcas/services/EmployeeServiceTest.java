package pi.oliveiras_multimarcas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));

        var result = employeeService.findAll();

        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));

        Employee result = employeeService.findById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchException.class, () -> employeeService.findById(UUID.randomUUID()));
    }

    @Test
    void testInsert() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("Maria");
        dto.setEmail("test@test.com");
        dto.setPassword("1234");
        dto.setPosition("Dev");
        dto.setContact("99999-9999");

        Employee saved = new Employee();
        saved.setId(UUID.randomUUID());

        when(passwordEncoder.encode("1234")).thenReturn("encodedPwd");
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        Employee result = employeeService.insert(dto);

        assertNotNull(result.getId());
        verify(passwordEncoder).encode("1234");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testUpdateByIdSuccess() {
        UUID id = UUID.randomUUID();

        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("Maria");
        dto.setEmail("maria@test.com");
        dto.setPassword("12345");
        dto.setContact("88888-8888");
        dto.setPosition("Manager");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(new Employee()));
        when(passwordEncoder.encode("12345")).thenReturn("encodedPwd");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(a -> a.getArgument(0));

        Employee result = employeeService.updateById(id, dto);

        assertEquals(id, result.getId());
        assertEquals("encodedPwd", result.getPassword());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testUpdateByIdNotFound() {
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchException.class, () -> {
            employeeService.updateById(UUID.randomUUID(), new EmployeeRequestDTO());
        });
    }

    @Test
    void testDeleteByIdSuccess() {
        UUID id = UUID.randomUUID();

        when(employeeRepository.existsById(id)).thenReturn(true);

        employeeService.deleteById(id);

        verify(employeeRepository).deleteById(id);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(employeeRepository.existsById(any())).thenReturn(false);

        assertThrows(NoSuchException.class, () -> employeeService.deleteById(UUID.randomUUID()));
    }

    @Test
    void testFindByEmailSuccess() {
        Employee emp = new Employee();
        emp.setEmail("test@test.com");

        when(employeeRepository.findByEmail("test@test.com")).thenReturn(Optional.of(emp));

        Employee result = employeeService.findByEmail("test@test.com");

        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchException.class, () -> employeeService.findByEmail("x@x.com"));
    }
}

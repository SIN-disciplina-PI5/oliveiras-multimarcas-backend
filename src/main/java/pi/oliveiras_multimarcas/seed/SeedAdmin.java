package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.services.EmployeeService;

@Component
@Profile("prod")
public class SeedAdmin implements CommandLineRunner {

    @Autowired
    private EmployeeService employeeService;
    @Override
    public void run(String... args){

        EmployeeRequestDTO employee1 = new EmployeeRequestDTO();
        employee1.setEmail("admin@admin.com");
        employee1.setPassword("admin");
        employee1.setName("Ewayrton");
        employee1.setPosition("Product Owner");
        employee1.setContact("081900000001");
        employee1.setCpf("12345678901");
        employee1.setRole(UserRole.ADMIN);

        employeeService.insert(employee1);
    }

}

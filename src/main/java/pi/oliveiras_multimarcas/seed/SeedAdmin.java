package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;
import pi.oliveiras_multimarcas.services.EmployeeService;

@Component
@Profile("prod")
public class SeedAdmin implements CommandLineRunner {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public void run(String... args){

        if (employeeRepository.existsByEmail("admin@admin.com")){
            EmployeeRequestDTO employee1 = new EmployeeRequestDTO();
            employee1.setEmail("admin@admin.com");
            employee1.setName("Ewayrton");
            employee1.setPosition("Product Owner");
            employee1.setContact("081900000001");
            employee1.setCpf("12345678901");
            employee1.setRole(UserRole.ADMIN);

            try{
                employeeService.insert(employee1);
            } catch (RuntimeException e){
                System.out.println("Error: "+ e.getMessage());
            }

        }




    }

}

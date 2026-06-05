package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.dto.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.dto.PreferencesRequestDTO;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.repositories.EmployeeRepository;
import pi.oliveiras_multimarcas.services.EmployeeService;
import pi.oliveiras_multimarcas.services.PreferencesService;

@Component
@Profile("prod")
public class SeedAdmin implements CommandLineRunner {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PreferencesService preferencesService;
    @Override
    public void run(String... args){

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

        Employee employee = employeeService.findByEmail("admin@admin.com");
        employeeService.changePassword(employee.getId(),"admin");

        PreferencesRequestDTO preferencesRequestDTO = getPreferencesRequestDTO();
        preferencesService.updatePreferences(preferencesRequestDTO);

    }

    private static PreferencesRequestDTO getPreferencesRequestDTO() {
        PreferencesRequestDTO preferencesRequestDTO = new PreferencesRequestDTO();
        preferencesRequestDTO.setAddress("Rua Nossa Senhora de Fátima, 28 - Jardim São Paulo, Recife - PE");
        preferencesRequestDTO.setUrlAddress("https://maps.app.goo.gl/cNPEU7RPPjkGY2697");
        preferencesRequestDTO.setCnpj("62.332.520/0001-59");
        preferencesRequestDTO.setContact("81994134298");
        preferencesRequestDTO.setEmail("oliveirasmultimarcasof@gmail.com");
        preferencesRequestDTO.setUrlInstagram("https://www.instagram.com/oliveiras_multimarcas_of/");
        return preferencesRequestDTO;
    }

}

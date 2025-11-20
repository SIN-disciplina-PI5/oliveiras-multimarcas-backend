package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.services.EmployeeService;
import pi.oliveiras_multimarcas.services.VehicleService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    VehicleService vehicleService;

    @Autowired
    EmployeeService userService;

    @Override
    public void run(String... args){

        VehicleRequestDTO vehicle1 = new VehicleRequestDTO();
        vehicle1.setModel("Civic EXL");
        vehicle1.setModelYear(2020);
        vehicle1.setPrice(new BigDecimal("125000.00"));
        List<String> images1 = new ArrayList<String>();

        images1.add("https://example1/1");
        images1.add("https://example1/2");
        images1.add("https://example1/3");

        vehicle1.setUrl_images(images1);
        vehicle1.setDescription("Sedan médio com ótimo desempenho, conforto e baixo consumo de combustível.");
        vehicle1.setMileage(32000);
        vehicle1.setMark("Honda");

        vehicleService.insert(vehicle1);

        VehicleRequestDTO vehicle2 = new VehicleRequestDTO();
        vehicle2.setModel("Corolla Altis Hybrid");
        vehicle2.setModelYear(2016);
        vehicle2.setPrice(new BigDecimal("145000.00"));
        List<String> images2 = new ArrayList<String>();

        images2.add("https://example2/1");
        images2.add("https://example2/2");
        images2.add("https://example2/3");

        vehicle2.setUrl_images(images2);
        vehicle2.setDescription("Versão híbrida do Corolla, unindo eficiência energética e conforto premium.");
        vehicle2.setMileage(21000);
        vehicle2.setMark("Toyota");

        vehicleService.insert(vehicle2);

        VehicleRequestDTO vehicle3 = new VehicleRequestDTO();
        vehicle3.setModel("Compass Longitude");
        vehicle3.setModelYear(2020);
        vehicle3.setPrice(new BigDecimal("138000.00"));
        List<String> images3 = new ArrayList<String>();

        images3.add("https://example3/1");
        images3.add("https://example3/2");
        images3.add("https://example3/3");

        vehicle3.setUrl_images(images3);
        vehicle3.setDescription("SUV robusto com tração 4x4 e interior sofisticado, ideal para viagens longas.");
        vehicle3.setMileage(12000);
        vehicle3.setMark("Jeep");

        vehicleService.insert(vehicle3);

        EmployeeRequestDTO user1 = new EmployeeRequestDTO();
        user1.setEmail("teste1@gmail.com");
        user1.setPassword("Senha1234");
        user1.setName("epamilondas");
        user1.setPosition("vendedor");
        user1.setContact("081900000000");
        user1.setRole(UserRole.ADMIN);

        userService.insert(user1);

        EmployeeRequestDTO user2 = new EmployeeRequestDTO();
        user2.setEmail("teste2@gmail.com");
        user2.setPassword("S3nh4123");
        user2.setName("Josefa");
        user2.setPosition("vendedor");
        user2.setContact("081900000001");
        user2.setRole(UserRole.ADMIN);

        userService.insert(user2);

    }

}

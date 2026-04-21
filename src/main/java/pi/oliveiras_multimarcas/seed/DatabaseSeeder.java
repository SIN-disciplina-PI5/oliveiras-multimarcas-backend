package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.DTO.*;
import pi.oliveiras_multimarcas.models.enums.Status;
import pi.oliveiras_multimarcas.models.enums.UserRole;
import pi.oliveiras_multimarcas.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    VehicleViewService vehicleViewService;

    @Autowired
    ClientService clientService;

    @Autowired
    SaleService saleService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    EmployeeService employeeService;

    @Override
    public void run(String... args){

        VehicleRequestDTO vehicle1 = new VehicleRequestDTO();
        vehicle1.setModel("Civic EXL");
        vehicle1.setModelYear(2020);
        vehicle1.setPrice(new BigDecimal("125000.00"));
        List<String> images1 = new ArrayList<String>();

        images1.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images1.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images1.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");

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

        images2.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images2.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images2.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");

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

        images3.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images3.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");
        images3.add("https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400&q=80");

        vehicle3.setUrl_images(images3);
        vehicle3.setDescription("SUV robusto com tração 4x4 e interior sofisticado, ideal para viagens longas.");
        vehicle3.setMileage(12000);
        vehicle3.setMark("Jeep");

        vehicleService.insert(vehicle3);

        EmployeeRequestDTO employee1 = new EmployeeRequestDTO();
        employee1.setEmail("teste1@gmail.com");
        employee1.setPassword("Senha1234");
        employee1.setName("epamilondas");
        employee1.setPosition("vendedor");
        employee1.setContact("081900000000");
        employee1.setRole(UserRole.ADMIN);

        employeeService.insert(employee1);

        EmployeeRequestDTO employee2 = new EmployeeRequestDTO();
        employee2.setEmail("teste2@gmail.com");
        employee2.setPassword("S3nh4123");
        employee2.setName("Josefa");
        employee2.setPosition("vendedor");
        employee2.setContact("081900000001");
        employee2.setRole(UserRole.ADMIN);

        employeeService.insert(employee2);

        ClientRequestDTO client1 = new ClientRequestDTO();
        client1.setUsername("Paulinha");
        client1.setEmail("paulinha@gmail.com");
        client1.setContact("081900000002");

        clientService.insert(client1);

        ClientRequestDTO client2 = new ClientRequestDTO();
        client2.setUsername("Roberval");
        client2.setEmail("roberval@gmail.com");
        client2.setContact("081900000003");

        clientService.insert(client2);

        SaleRequestDTO sale1 = new SaleRequestDTO();
        sale1.setClient(clientService.findByEmail(client1.getEmail()).getId());
        sale1.setVehicle(vehicleService.findAll().getFirst().getId());

        saleService.insert(sale1);

        SaleRequestDTO sale2 = new SaleRequestDTO();
        sale2.setClient(clientService.findByEmail(client2.getEmail()).getId());
        sale2.setVehicle(vehicleService.findAll().get(1).getId());

        saleService.insert(sale2);

        AppointmentRequestDTO appointment1 = new AppointmentRequestDTO();

        appointment1.setClientId(clientService.findByEmail(client1.getEmail()).getId());
        appointment1.setVehicleId(vehicleService.findAll().get(2).getId());
        appointment1.setStatus(Status.PENDING);
        appointment1.setDescription("descrição aqui");
        appointment1.setSchedulingTime(LocalTime.of(9, 0,0));
        appointment1.setSchedulingDate(LocalDate.of(2026,5, 12));

        appointmentService.insert(appointment1);

        AppointmentRequestDTO appointment2 = new AppointmentRequestDTO();

        appointment2.setClientId(clientService.findByEmail(client2.getEmail()).getId());
        appointment2.setVehicleId(vehicleService.findAll().get(2).getId());
        appointment2.setStatus(Status.PENDING);
        appointment2.setDescription("descrição aqui");
        appointment2.setSchedulingTime(LocalTime.of(9, 0,0));
        appointment2.setSchedulingDate(LocalDate.of(2026,5, 13));

        appointmentService.insert(appointment2);

        for (int i = 0; i < 20; i++) {
            vehicleViewService.insert(vehicleService.findAll().getFirst().getId());
            if (i%2==0){
                vehicleViewService.insert(vehicleService.findAll().get(2).getId());
            }
            if (i%5==0){
                vehicleViewService.insert(vehicleService.findAll().get(1).getId());
            }
        }
    }
}
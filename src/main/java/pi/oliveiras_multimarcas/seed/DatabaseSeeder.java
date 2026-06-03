package pi.oliveiras_multimarcas.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pi.oliveiras_multimarcas.dto.*;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.models.enums.AppointmentStatus;
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
        employee1.setName("epamilondas");
        employee1.setPosition("vendedor");
        employee1.setContact("081900000000");
        employee1.setCpf("12345678902");
        employee1.setRole(UserRole.ADMIN);

        Employee emp1 = employeeService.insert(employee1);
        employeeService.changePassword(emp1.getId(), "Senha1234");

        EmployeeRequestDTO employee2 = new EmployeeRequestDTO();
        employee2.setEmail("teste2@gmail.com");
        employee2.setName("Josefa");
        employee2.setPosition("vendedor");
        employee2.setContact("081900000001");
        employee2.setCpf("12345678903");
        employee2.setRole(UserRole.ADMIN);

        Employee emp2 = employeeService.insert(employee2);
        employeeService.changePassword(emp2.getId(), "Senha1234");

        ClientRequestDTO client1 = new ClientRequestDTO();
        client1.setName("Paulinha");
        client1.setEmail("paulinha@gmail.com");
        client1.setContact("081900000002");
        client1.setCpf("12345678900");
        clientService.insert(client1);

        ClientRequestDTO client2 = new ClientRequestDTO();
        client2.setName("Roberval");
        client2.setEmail("roberval@gmail.com");
        client2.setContact("081900000003");
        client2.setCpf("12345678901");
        clientService.insert(client2);

        ClientRequestDTO client3 = new ClientRequestDTO();
        client3.setName("Roberval");
        client3.setEmail("robervl@gmail.com");
        client3.setContact("081900000004");
        client3.setCpf("12345678983");
        clientService.insert(client3);

        ClientRequestDTO client4 = new ClientRequestDTO();
        client4.setName("Roberval");
        client4.setEmail("roberval2@gmail.com");
        client4.setContact("081900000182");
        client4.setCpf("12345678929");
        clientService.insert(client4);

        ClientRequestDTO client5 = new ClientRequestDTO();
        client5.setName("Roberval");
        client5.setEmail("roberval3@gmail.com");
        client5.setContact("081900000473");
        client5.setCpf("12345678283");
        clientService.insert(client5);

        ClientRequestDTO client6 = new ClientRequestDTO();
        client6.setName("Roberval");
        client6.setEmail("roberval4@gmail.com");
        client6.setContact("081900000827");
        client6.setCpf("12345678019");
        clientService.insert(client6);

        ClientRequestDTO client7 = new ClientRequestDTO();
        client7.setName("Roberval");
        client7.setEmail("roberval5@gmail.com");
        client7.setContact("081900000829");
        client7.setCpf("12345678028");
        clientService.insert(client7);

        SaleRequestDTO sale1 = new SaleRequestDTO();
        sale1.setClient(clientService.findByEmail(client1.getEmail()).getId());
        sale1.setVehicle(vehicleService.findAll().getFirst().getId());

        saleService.insert(sale1);

        SaleRequestDTO sale2 = new SaleRequestDTO();
        sale2.setClient(clientService.findByEmail(client2.getEmail()).getId());
        sale2.setVehicle(vehicleService.findAll().get(1).getId());

        saleService.insert(sale2);

        AppointmentRequestDTO appointment1 = new AppointmentRequestDTO();

        appointment1.setClient(client1);
        appointment1.setVehicleId(vehicleService.findAll().get(2).getId());
        appointment1.setStatus(AppointmentStatus.PENDING);
        appointment1.setSchedulingTime(LocalTime.of(9, 0,0));
        appointment1.setSchedulingDate(LocalDate.of(2026,5, 12));

        appointmentService.insert(appointment1);

        AppointmentRequestDTO appointment2 = new AppointmentRequestDTO();

        appointment2.setClient(client2);
        appointment2.setVehicleId(vehicleService.findAll().get(2).getId());
        appointment2.setStatus(AppointmentStatus.PENDING);
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
package pi.oliveiras_multimarcas.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pi.oliveiras_multimarcas.dto.DashboardResponseDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.repositories.AppointmentRepository;
import pi.oliveiras_multimarcas.repositories.ClientRepository;
import pi.oliveiras_multimarcas.repositories.SaleRepository;
import pi.oliveiras_multimarcas.repositories.VehicleBrandCount;
import pi.oliveiras_multimarcas.repositories.VehicleRepositorie;
import pi.oliveiras_multimarcas.services.DashboardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private VehicleRepositorie vehicleRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboardData_Success() {
        Sale sale = new Sale();
        sale.setId(java.util.UUID.randomUUID());
        Vehicle vehicle = new Vehicle();
        vehicle.setPrice(new BigDecimal("50000.00"));
        sale.setVehicle(vehicle);

        Client client = new Client();
        client.setId(java.util.UUID.randomUUID());
        sale.setClient(client);
        vehicle.setId(java.util.UUID.randomUUID());

        List<Sale> sales = List.of(sale);
        when(saleRepository.findBySaleDateBetweenOrderBySaleDateDesc(any(Date.class), any(Date.class))).thenReturn(sales);

        // Avoid mapping exceptions: create full appointment object if mapped
        Appointment appointment = new Appointment();
        appointment.setId(java.util.UUID.randomUUID());
        appointment.setVehicle(vehicle);
        appointment.setClient(client);
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepository.findBySchedulingDateBetweenOrderBySchedulingDateDesc(any(), any())).thenReturn(appointments);

        when(vehicleRepository.countCarsByBrand()).thenReturn(new ArrayList<>());
        when(clientRepository.count()).thenReturn(10L);

        DashboardResponseDTO result = dashboardService.getDashboardData("2024-01-01", "2024-12-31");

        assertNotNull(result);
        assertEquals(1, result.getSales().getTotalSales());
        assertEquals(new BigDecimal("50000.00"), result.getSales().getTotalRevenue());
        assertEquals(1, result.getVisits().getTotalVisits());
        assertEquals(10L, result.getTotalCustomers());
    }

    @Test
    void getDashboardData_InvalidDates() {
        assertThrows(InvalidArguments.class, () -> dashboardService.getDashboardData(null, "2024-12-31"));
        assertThrows(InvalidArguments.class, () -> dashboardService.getDashboardData("invalid-date", "2024-12-31"));
        assertThrows(InvalidArguments.class, () -> dashboardService.getDashboardData("2024-12-31", "2024-01-01"));
    }
}
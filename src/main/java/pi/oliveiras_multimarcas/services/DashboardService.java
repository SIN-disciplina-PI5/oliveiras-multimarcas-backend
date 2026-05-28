package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi.oliveiras_multimarcas.dto.*;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.repositories.AppointmentRepository;
import pi.oliveiras_multimarcas.repositories.ClientRepository;
import pi.oliveiras_multimarcas.repositories.SaleRepository;
import pi.oliveiras_multimarcas.repositories.VehicleBrandCount;
import pi.oliveiras_multimarcas.repositories.VehicleRepositorie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VehicleRepositorie vehicleRepository;

    @Autowired
    private ClientRepository clientRepository;

    public DashboardResponseDTO getDashboardData(String startDateStr, String endDateStr) {
        if (startDateStr == null || endDateStr == null) {
            throw new InvalidArguments("start e end (são obrigatórios)");
        }

        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDateStr);
            end = LocalDate.parse(endDateStr);
        } catch (Exception e) {
            throw new InvalidArguments("data (formato inválido, use yyyy-MM-dd)");
        }

        if (start.isAfter(end)) {
            throw new InvalidArguments("start (não pode ser maior que end)");
        }

        // Convert LocalDate to java.util.Date for the Sale repository (which uses Date)
        // start of day
        Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // end of day -> next day at 00:00:00 minus 1 millisecond or just using LocalTime.MAX
        Date endDate = Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        // 1. Sales
        List<Sale> sales = saleRepository.findBySaleDateBetweenOrderBySaleDateDesc(startDate, endDate);
        long totalSales = sales.size();
        BigDecimal totalRevenue = sales.stream()
                .map(sale -> sale.getVehicle() != null ? sale.getVehicle().getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<SaleResponseDTO> saleHistory = sales.stream()
                .map(SaleResponseDTO::new)
                .collect(Collectors.toList());
        DashboardSalesDTO salesDTO = new DashboardSalesDTO(totalSales, totalRevenue, saleHistory);

        // 2. Visits / Test-drives
        List<Appointment> appointments = appointmentRepository.findBySchedulingDateBetweenOrderBySchedulingDateDesc(start, end);
        long totalVisits = appointments.size();
        List<AppointmentResponseDTO> visitHistory = appointments.stream()
                .map(AppointmentResponseDTO::new)
                .collect(Collectors.toList());
        DashboardVisitsDTO visitsDTO = new DashboardVisitsDTO(totalVisits, visitHistory);

        // 3. Cars by Brand
        List<VehicleBrandCount> brandCounts = vehicleRepository.countCarsByBrand();
        Map<String, Long> carsByBrand = brandCounts.stream()
                .collect(Collectors.toMap(VehicleBrandCount::getMark, VehicleBrandCount::getBrandCount));

        // 4. Total Customers
        long totalCustomers = clientRepository.count();

        // 5. Build Response
        return new DashboardResponseDTO(salesDTO, visitsDTO, carsByBrand, totalCustomers);
    }
}
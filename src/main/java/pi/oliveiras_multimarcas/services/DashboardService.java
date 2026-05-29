package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi.oliveiras_multimarcas.dto.*;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.models.Appointment;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.repositories.AppointmentRepository;
import pi.oliveiras_multimarcas.repositories.ClientRepository;
import pi.oliveiras_multimarcas.repositories.ProposalRepository;
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

    @Autowired
    private ProposalRepository proposalRepository;

    public DashboardResponseDTO getDashboardData(String startDateStr, String endDateStr) {

        // Valida se as datas foram enviadas
        if (startDateStr == null || endDateStr == null) {
            throw new InvalidArguments("start e end (são obrigatórios)");
        }

        LocalDate start;
        LocalDate end;

        try {

            // Converte as Strings para LocalDate
            start = LocalDate.parse(startDateStr);
            end = LocalDate.parse(endDateStr);

        } catch (Exception e) {

            // Lança erro caso o formato da data esteja inválido
            throw new InvalidArguments("data (formato inválido, use yyyy-MM-dd)");
        }

        // Verifica se a data inicial é maior que a final
        if (start.isAfter(end)) {
            throw new InvalidArguments("start (não pode ser maior que end)");
        }

        // Converte LocalDate para java.util.Date
        // Início do dia
        Date startDate = Date.from(
                start.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        // Final do dia
        Date endDate = Date.from(
                end.atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        // =========================================================
        // 1. VENDAS
        // =========================================================

        // Busca vendas no período informado
        List<Sale> sales = saleRepository
                .findBySaleDateBetweenOrderBySaleDateDesc(startDate, endDate);

        // Quantidade total de vendas
        long totalSales = sales.size();

        // Soma o valor total vendido
        BigDecimal totalRevenue = sales.stream()
                .map(sale ->
                        sale.getVehicle() != null
                                ? sale.getVehicle().getPrice()
                                : BigDecimal.ZERO
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Converte as vendas para DTO
        List<SaleResponseDTO> saleHistory = sales.stream()
                .map(SaleResponseDTO::new)
                .collect(Collectors.toList());

        // Cria DTO de vendas do dashboard
        DashboardSalesDTO salesDTO = new DashboardSalesDTO(
                totalSales,
                totalRevenue,
                saleHistory
        );

        // =========================================================
        // 2. VISITAS / TEST-DRIVES
        // =========================================================

        // Busca agendamentos no período informado
        List<Appointment> appointments = appointmentRepository
                .findBySchedulingDateBetweenOrderBySchedulingDateDesc(start, end);

        // Total de visitas
        long totalVisits = appointments.size();

        // Converte agendamentos para DTO
        List<AppointmentResponseDTO> visitHistory = appointments.stream()
                .map(AppointmentResponseDTO::new)
                .collect(Collectors.toList());

        // Cria DTO de visitas do dashboard
        DashboardVisitsDTO visitsDTO = new DashboardVisitsDTO(
                totalVisits,
                visitHistory
        );

        // =========================================================
        // 3. PROPOSTAS
        // =========================================================

        // Total geral de propostas
        long totalProposals = proposalRepository.count();

        // Total de propostas aceitas
        long totalAccepted = proposalRepository
                .countByStatus(ProposalStatus.ACCEPTED);

        // Total de propostas rejeitadas
        long totalRejected = proposalRepository
                .countByStatus(ProposalStatus.REJECTED);

        // Total de propostas expiradas
        long totalExpired = proposalRepository
                .countByStatus(ProposalStatus.EXPIRED);

        // Total de propostas pendentes
        long totalPending = proposalRepository
                .countByStatus(ProposalStatus.PENDING);

        // Calcula a taxa de conversão
        double conversionRate = totalProposals > 0
                ? ((double) totalAccepted / totalProposals) * 100
                : 0.0;

        // Cria DTO de propostas do dashboard
        DashboardProposalsDTO proposalsDTO =
                new DashboardProposalsDTO(
                        totalProposals,
                        totalAccepted,
                        totalRejected,
                        totalExpired,
                        totalPending,
                        conversionRate
                );

        // =========================================================
        // 4. CARROS POR MARCA
        // =========================================================

        // Busca quantidade de carros agrupados por marca
        List<VehicleBrandCount> brandCounts =
                vehicleRepository.countCarsByBrand();

        // Converte para Map<String, Long>
        Map<String, Long> carsByBrand = brandCounts.stream()
                .collect(Collectors.toMap(
                        VehicleBrandCount::getMark,
                        VehicleBrandCount::getBrandCount
                ));

        // =========================================================
        // 5. TOTAL DE CLIENTES
        // =========================================================

        // Conta total de clientes cadastrados
        long totalCustomers = clientRepository.count();

        // =========================================================
        // 6. RETORNO FINAL DO DASHBOARD
        // =========================================================

        return new DashboardResponseDTO(
                salesDTO,
                visitsDTO,
                proposalsDTO,
                carsByBrand,
                totalCustomers
        );
    }
}
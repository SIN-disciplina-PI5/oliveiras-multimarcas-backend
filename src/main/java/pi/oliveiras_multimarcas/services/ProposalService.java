package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.dto.ProposalRequestDTO;
import pi.oliveiras_multimarcas.dto.ProposalResponseDTO;
import pi.oliveiras_multimarcas.dto.SaleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Proposal;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;
import pi.oliveiras_multimarcas.repositories.ProposalRepository;
import pi.oliveiras_multimarcas.repositories.VehicleRepositorie;
import pi.oliveiras_multimarcas.services.S3Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepositorie vehicleRepository;
    @Autowired
    private SaleService saleService;
    @Autowired
    private S3Service s3Service;

    @Transactional
    public Proposal create(ProposalRequestDTO dto) {
        Client client = clientService.findById(dto.getClientId());
        Vehicle vehicle = vehicleService.findyById(dto.getVehicleId());

        if (!vehicle.isAvailable()) {
            throw new InvalidArguments("Veículo não está mais disponível para negociação.");
        }

        Proposal proposal = new Proposal();
        proposal.setClient(client);
        proposal.setVehicle(vehicle);
        proposal.setEntryValue(dto.getEntryValue());
        proposal.setInstallments(dto.getInstallments());
        proposal.setObservations(dto.getObservations());

        // Regra de Juros (Definido pelo DTO ou fixado em 2% ao mês)
        BigDecimal interestRate = dto.getInterestRate() != null ? dto.getInterestRate() : new BigDecimal("0.02");
        proposal.setInterestRate(interestRate);

        // Cálculos financeiros (Juros Simples para exemplo)
        BigDecimal amountToFinance = vehicle.getPrice().subtract(dto.getEntryValue());
        if (amountToFinance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidArguments("Valor de entrada maior que o valor do veículo.");
        }

        BigDecimal interestMultiplier = BigDecimal.ONE.add(interestRate.multiply(new BigDecimal(dto.getInstallments())));
        BigDecimal totalFinanced = amountToFinance.multiply(interestMultiplier);

        BigDecimal installmentValue = totalFinanced.divide(new BigDecimal(dto.getInstallments()), 2, RoundingMode.HALF_UP);
        BigDecimal totalValue = dto.getEntryValue().add(totalFinanced);

        proposal.setInstallmentValue(installmentValue);
        proposal.setTotalValue(totalValue);

        // Expira em 7 dias
        proposal.setExpirationDate(LocalDateTime.now().plusDays(7));
        proposal.setStatus(ProposalStatus.PENDING);

        return proposalRepository.save(proposal);
    }

    @Transactional(readOnly = true)
    public Proposal findById(UUID id) {
        return proposalRepository.findById(id).orElseThrow(() -> new NoSuchException("Proposta"));
    }

    @Transactional
    public ProposalResponseDTO updateContractUrl(UUID id, String contractUrl) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Proposta não encontrada com o ID informado."));

        proposal.setContractUrl(contractUrl);
        Proposal updatedProposal = proposalRepository.save(proposal);

        return new ProposalResponseDTO(updatedProposal);
    }

    @Transactional(readOnly = true)
    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Proposal> findByStatus(ProposalStatus status) {
        return proposalRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Proposal> findByClient(UUID clientId) {
        return proposalRepository.findByClientId(clientId);
    }

    @Transactional
    public Proposal accept(UUID id) {
        Proposal proposal = findById(id);
        validateProposalIsPending(proposal);

        proposal.setStatus(ProposalStatus.ACCEPTED);

        // 1. Marcar veículo como vendido/indisponível
        Vehicle vehicle = proposal.getVehicle();
        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        // 2. Gerar a Venda Oficial (Sale)
        SaleRequestDTO saleDto = new SaleRequestDTO();
        saleDto.setClient(proposal.getClient().getId());
        saleDto.setVehicle(vehicle.getId());
        saleService.insert(saleDto);

        // 3. Cancelar outras propostas pendentes para o mesmo veículo (Opcional, mas recomendado)
        // Lógica a ser implementada caso deseje.

        return proposalRepository.save(proposal);
    }

    @Transactional
    public Proposal reject(UUID id) {
        Proposal proposal = findById(id);
        validateProposalIsPending(proposal);
        proposal.setStatus(ProposalStatus.REJECTED);
        return proposalRepository.save(proposal);
    }

    @Transactional
    public Proposal cancel(UUID id) {
        Proposal proposal = findById(id);
        validateProposalIsPending(proposal);
        proposal.setStatus(ProposalStatus.CANCELED);
        return proposalRepository.save(proposal);
    }

    private void validateProposalIsPending(Proposal proposal) {
        if (proposal.getStatus() != ProposalStatus.PENDING) {
            throw new InvalidArguments("Ação não permitida. A proposta está com status: " + proposal.getStatus());
        }
        if (proposal.getExpirationDate().isBefore(LocalDateTime.now())) {
            proposal.setStatus(ProposalStatus.EXPIRED);
            proposalRepository.save(proposal);
            throw new InvalidArguments("A proposta já expirou.");
        }
    }

    // Cron job para rodar todos os dias à meia-noite e expirar propostas vencidas
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireOldProposals() {
        List<Proposal> expiredProposals = proposalRepository.findByStatusAndExpirationDateBefore(
                ProposalStatus.PENDING, LocalDateTime.now());

        expiredProposals.forEach(p -> p.setStatus(ProposalStatus.EXPIRED));
        proposalRepository.saveAll(expiredProposals);
    }

    @Transactional
    public void cancelAndRemove(UUID id) {
        Proposal proposal = findById(id);

        // Se existir um contrato, removemos do S3 (assumindo que tem o nome do ficheiro ou URL)
        if (proposal.getContractUrl() != null) {
            // Implemente a lógica no seu S3Service para deletar pela URL
            s3Service.deleteFile(proposal.getContractUrl());
        }

        proposalRepository.delete(proposal);
    }
}
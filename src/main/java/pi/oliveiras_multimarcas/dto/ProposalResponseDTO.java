package pi.oliveiras_multimarcas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Proposal;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProposalResponseDTO {
    private UUID id;
    private UUID clientId;
    private String clientName;
    private UUID vehicleId;
    private String vehicleModel;
    private BigDecimal entryValue;
    private int installments;
    private BigDecimal installmentValue;
    private BigDecimal interestRate;
    private BigDecimal totalValue;
    private String observations;
    private LocalDateTime expirationDate;
    private ProposalStatus status;
    private LocalDateTime createdAt;

    public ProposalResponseDTO(Proposal proposal) {
        this.id = proposal.getId();
        this.clientId = proposal.getClient().getId();
        this.clientName = proposal.getClient().getName();
        this.vehicleId = proposal.getVehicle().getId();
        this.vehicleModel = proposal.getVehicle().getModel();
        this.entryValue = proposal.getEntryValue();
        this.installments = proposal.getInstallments();
        this.installmentValue = proposal.getInstallmentValue();
        this.interestRate = proposal.getInterestRate();
        this.totalValue = proposal.getTotalValue();
        this.observations = proposal.getObservations();
        this.expirationDate = proposal.getExpirationDate();
        this.status = proposal.getStatus();
        this.createdAt = proposal.getCreatedAt();
    }
}
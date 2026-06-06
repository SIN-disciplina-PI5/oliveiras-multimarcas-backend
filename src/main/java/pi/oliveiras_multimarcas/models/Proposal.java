package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "proposals")
public class Proposal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="vehicle_id", nullable=false)
    private Vehicle vehicle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal entryValue; // Valor de entrada

    @Column(nullable = false)
    private int installments; // Quantidade de parcelas

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal installmentValue; // Valor da parcela

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate; // Taxa de juros (ex: 0.05 para 5%)

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalValue; // Valor total final

    @Column(length = 500)
    private String observations;

    @Column(nullable = false)
    private LocalDateTime expirationDate; // Data de validade da proposta

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status = ProposalStatus.PENDING;

    @Column(name = "contract_url", length = 500)
    private String contractUrl;
}
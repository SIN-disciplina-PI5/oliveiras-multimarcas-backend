package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Proposal;
import pi.oliveiras_multimarcas.models.enums.ProposalStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    List<Proposal> findByStatus(ProposalStatus status);
    List<Proposal> findByClientId(UUID clientId);
    List<Proposal> findByStatusAndExpirationDateBefore(ProposalStatus status, LocalDateTime dateTime);

    // Nova consulta adicionada para suportar a filtragem por múltiplos status operacionais
    List<Proposal> findByStatusIn(List<ProposalStatus> statuses);

    long countByStatus(ProposalStatus status);
}
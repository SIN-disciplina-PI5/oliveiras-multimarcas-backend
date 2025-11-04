package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Sale;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepositorie extends JpaRepository<Sale, UUID> {
    public Optional<Sale> findByEmail(String email);
}

package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Client;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    public Optional<Client> findByEmail(String email);
}

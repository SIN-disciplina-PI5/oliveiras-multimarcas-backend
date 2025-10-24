package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Token;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepositorie extends JpaRepository<Token, UUID> {
    public Optional<Token> findByToken(String token);
    public void deleteByToken(String token);
}

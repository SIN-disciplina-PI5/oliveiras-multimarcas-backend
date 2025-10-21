package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.oliveiras_multimarcas.models.Token;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepositorie extends JpaRepository<Token, UUID> {
    public Optional<Token> findByToken(String token);
    public void deleteByToken(String token);
}

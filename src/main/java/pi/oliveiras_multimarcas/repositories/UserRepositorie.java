package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositorie extends JpaRepository<User, UUID> {
    public Optional<User> findByEmail(String email);
}

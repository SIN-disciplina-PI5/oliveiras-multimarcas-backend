package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.oliveiras_multimarcas.models.User;

import java.util.UUID;

public interface UserRepositorie extends JpaRepository<User, UUID> {
}

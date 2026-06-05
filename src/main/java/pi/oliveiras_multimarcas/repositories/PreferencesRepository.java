package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
}

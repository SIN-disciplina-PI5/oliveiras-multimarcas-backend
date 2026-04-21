package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.CarView;

@Repository
public interface CarViewRepository extends JpaRepository<CarView, Integer> {
}

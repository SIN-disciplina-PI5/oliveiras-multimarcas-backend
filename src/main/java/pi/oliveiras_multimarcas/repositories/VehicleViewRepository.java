package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.VehicleView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleViewRepository extends JpaRepository<VehicleView, Long> {
    public Optional<List<VehicleView>> findByVehicleId(UUID id);

    @Query(value = """
    SELECT vehicle_id, COUNT(*) as total
    FROM vehicle_view
    GROUP BY vehicle_id
    ORDER BY total DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> findTop5Vehicles();
}

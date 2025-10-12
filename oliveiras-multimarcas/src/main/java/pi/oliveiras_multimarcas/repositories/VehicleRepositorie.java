package pi.oliveiras_multimarcas.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pi.oliveiras_multimarcas.models.Vehicle;

@Repository
public interface VehicleRepositorie extends JpaRepository<Vehicle, UUID>{

}

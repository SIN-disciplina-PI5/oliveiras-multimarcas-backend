package pi.oliveiras_multimarcas.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pi.oliveiras_multimarcas.models.Vehicle;

@Repository
public interface VehicleRepositorie extends JpaRepository<Vehicle, UUID>{
    
    @Query("SELECT v.mark AS mark, COUNT(v) AS brandCount FROM Vehicle v GROUP BY v.mark")
    List<VehicleBrandCount> countCarsByBrand();
}
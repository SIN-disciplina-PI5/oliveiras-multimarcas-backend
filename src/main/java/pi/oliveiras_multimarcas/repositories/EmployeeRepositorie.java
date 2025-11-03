package pi.oliveiras_multimarcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.oliveiras_multimarcas.models.Employee;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepositorie extends JpaRepository<Employee, UUID> {
    public Optional<Employee> findByEmail(String email);
}

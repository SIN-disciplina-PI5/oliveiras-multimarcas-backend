package pi.oliveiras_multimarcas.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.repositories.VehicleRepositorie;


@Service
public class VehicleService {

    @Autowired
    private VehicleRepositorie vehicleRepository;

    @Transactional(readOnly=true)
    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly=true)
    public Vehicle findyById(UUID id){

        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (!vehicle.isPresent()) {
            throw new NoSuchException("Veículo");
        }

        return vehicle.get();
    }

    @Transactional
    public Vehicle insert(VehicleRequestDTO dto){

        Vehicle vehicle = new Vehicle(dto);

        vehicle = vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Transactional
    public void delete(UUID id){
        if(!vehicleRepository.existsById(id)){
            throw new NoSuchException("Veículo");
        }

        vehicleRepository.deleteById(id);
    }

    @Transactional
    public Vehicle update(VehicleRequestDTO dto, UUID id){

        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if(!vehicle.isPresent()){
            throw new NoSuchException("Veículo");
        }
        Vehicle newVehicle = new Vehicle(dto);
        newVehicle.setId(id);
        return newVehicle;

    }
}

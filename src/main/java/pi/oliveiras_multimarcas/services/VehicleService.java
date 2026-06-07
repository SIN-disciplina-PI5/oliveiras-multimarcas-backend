package pi.oliveiras_multimarcas.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pi.oliveiras_multimarcas.dto.VehicleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.ImageVehicle;
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
        if (vehicle.isEmpty()) throw new NoSuchException("Veículo");


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
    public Vehicle update(VehicleRequestDTO dto, UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Veículo"));

        vehicle.setModel(dto.getModel());
        vehicle.setPrice(dto.getPrice());
        vehicle.setDescription(dto.getDescription());
        vehicle.setMark(dto.getMark());
        vehicle.setMileage(dto.getMileage());
        vehicle.setModelYear(dto.getModelYear());
        Set<String> existingUrls = vehicle.getUrl_images()
                .stream()
                .map(ImageVehicle::getUrl)
                .collect(Collectors.toSet());

        for (String url : dto.getUrl_images()) {

            if (!existingUrls.contains(url)) {

                ImageVehicle image = new ImageVehicle(url, vehicle);

                vehicle.getUrl_images().add(image);
            }
        }

        return vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAllAvailable() {
        return vehicleRepository.findByAvailableTrue();
    }
}

package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.models.VehicleView;
import pi.oliveiras_multimarcas.repositories.VehicleViewRepository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleViewService {
    @Autowired
    private VehicleViewRepository vehicleViewRepository;

    @Autowired
    private VehicleService vehicleService;

    @Transactional
    public void insert(UUID id){
        Vehicle vehicle = vehicleService.findyById(id);
        VehicleView vehicleView = new VehicleView(vehicle);
        vehicleViewRepository.save(vehicleView);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findTop5Vehicles(){
        List<Object[]> vehicleViews = vehicleViewRepository.findTop5Vehicles();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        for (Object[] obj: vehicleViews){
            byte[] bytes = (byte[]) obj[0];
            UUID id = bytesToUUID(bytes);
            Vehicle vehicle = vehicleService.findyById(id);
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    public static UUID bytesToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}

package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.SaleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.models.Vehicle;
import pi.oliveiras_multimarcas.repositories.SaleRepository;

import java.util.List;
import java.util.UUID;

@Service
public class SaleService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private SaleRepository SaleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Sale> findAll(){
        return SaleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sale findById(UUID id){
        return SaleRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    @Transactional
    public Sale insert(SaleRequestDTO dto) {
        Sale sale = toEntity(dto);
        sale = SaleRepository.save(sale);
        return sale;
    }

    @Transactional
    public Sale updateById(UUID id, SaleRequestDTO dto) {
        SaleRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Sale sale = toEntity(dto);
        sale.setId(id);
        sale = SaleRepository.save(sale);
        return sale;
    }

    @Transactional
    public void deleteById(UUID id){
        if(!SaleRepository.existsById(id)){
            throw new NoSuchException("Usuário");
        }
        SaleRepository.deleteById(id);
    }


    private Sale toEntity(SaleRequestDTO dto) {
        Sale sale = new Sale();
        Client client;
        Vehicle vehicle;
        try{
            client =  clientService.findById(dto.getClient());
            vehicle = vehicleService.findyById(dto.getVehicle());
        }catch (NoSuchException e){
            throw new RuntimeException(e.getMessage());
        }
        sale.setSaleDate(dto.getSaleDate());
        sale.setClient(client);
        sale.setVehicle(vehicle);
        return sale;
    }
}

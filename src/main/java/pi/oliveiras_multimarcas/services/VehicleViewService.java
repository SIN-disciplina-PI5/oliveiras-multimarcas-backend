package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.CarViewRequestDTO;
import pi.oliveiras_multimarcas.models.CarView;
import pi.oliveiras_multimarcas.repositories.CarViewRepository;

@Service
public class CarViewService {
    @Autowired
    private CarViewRepository carViewRepository;

    @Transactional
    public void insert(CarViewRequestDTO carViewRequestDTO){


    }

    public CarView toEntity(CarViewRequestDTO carViewRequestDTO){

    }
}

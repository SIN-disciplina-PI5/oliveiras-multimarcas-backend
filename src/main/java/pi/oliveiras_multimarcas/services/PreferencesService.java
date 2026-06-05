package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.dto.PreferencesRequestDTO;
import pi.oliveiras_multimarcas.models.Preferences;
import pi.oliveiras_multimarcas.dto.PreferensesResponseDTO;
import pi.oliveiras_multimarcas.repositories.PreferencesRepository;

@Service
public class PreferencesService {

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Transactional(readOnly = true)
    public PreferensesResponseDTO getPreferences(){
        Preferences preferences = preferencesRepository.findAll().getFirst();
        return new PreferensesResponseDTO(preferences);
    }

    @Transactional
    public PreferensesResponseDTO updatePreferences(PreferencesRequestDTO dto){
        try{
            Preferences preferences = preferencesRepository.findAll().getFirst();
            preferences.setAddress(dto.getAddress());
            preferences.setUrlAddress(dto.getUrlAddress());
            preferences.setCnpj(dto.getCnpj());
            preferences.setEmail(dto.getEmail());
            preferences.setUrlInstagram(dto.getUrlInstagram());
            preferences.setContact(dto.getContact());
            Preferences updatedPreferences = preferencesRepository.save(preferences);
            return new PreferensesResponseDTO(updatedPreferences);
        } catch (RuntimeException e) {
            Preferences firstPreference = new Preferences();
            firstPreference.setAddress(dto.getAddress());
            firstPreference.setUrlAddress(dto.getUrlAddress());
            firstPreference.setCnpj(dto.getCnpj());
            firstPreference.setEmail(dto.getEmail());
            firstPreference.setUrlInstagram(dto.getUrlInstagram());
            firstPreference.setContact(dto.getContact());
            Preferences savedPreferences = preferencesRepository.save(firstPreference);
            return new PreferensesResponseDTO(savedPreferences);
        }



    }
}

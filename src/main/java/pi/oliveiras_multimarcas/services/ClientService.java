package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.ClientRequestDTO;
import pi.oliveiras_multimarcas.DTO.ClientResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Client;
import pi.oliveiras_multimarcas.repositories.ClientRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository
                .findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(UUID id) {

        return clientRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchException("Cliente"));
    }

    @Transactional
    public Client insert(ClientRequestDTO dto) {
        Client client = toEntity(dto);
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client = clientRepository.save(client);
        return client;
    }

    @Transactional
    public Client updateById(UUID id, ClientRequestDTO dto) {
        clientRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Client client = toEntity(dto);
        client.setId(id);
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client = clientRepository
                .save(client);
        return client;
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!clientRepository
                .existsById(id)) {
            throw new NoSuchException("Usuário");
        }
        clientRepository
                .deleteById(id);
    }

    public Client findByEmail(String email) {
        return clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private Client toEntity(ClientRequestDTO dto) {
        Client client = new Client();
        client.setName(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setPassword(dto.getPassword());
        client.setContact(dto.getContact());
        client.setRole(dto.getRole());
        return client;
    }
}

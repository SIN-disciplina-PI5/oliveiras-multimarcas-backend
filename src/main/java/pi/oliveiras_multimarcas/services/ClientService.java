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
    private ClientRepository ClientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Busca e retorna todos os usuários cadastrados.
     *
     * @return Uma lista de {@link ClientResponseDTO} com os dados de todos os
     *         usuários.
     */
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return ClientRepository
                .findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O UUID do usuário a ser buscado.
     * @return Um {@link ClientResponseDTO} com os dados do usuário encontrado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(UUID id) {
        Client Client = ClientRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchException("Cliente"));
        return toResponseDTO(Client);
    }

    /**
     * Cria um novo usuário no sistema.
     * A senha do usuário é codificada antes de ser salva.
     *
     * @param dto O {@link ClientRequestDTO} com os dados do usuário a ser criado.
     * @return Um {@link ClientResponseDTO} com os dados do usuário recém-criado.
     */
    @Transactional
    public ClientResponseDTO insert(ClientRequestDTO dto) {
        Client Client = toEntity(dto);
        Client.setPassword(passwordEncoder.encode(dto.getPassword()));
        Client = ClientRepository
                .save(Client);
        return toResponseDTO(Client);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id  O UUID do usuário a ser atualizado.
     * @param dto O {@link ClientRequestDTO} com os novos dados do usuário.
     * @return Um {@link ClientResponseDTO} com os dados do usuário atualizado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public ClientResponseDTO updateById(UUID id, ClientRequestDTO dto) {
        ClientRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Client Client = toEntity(dto);
        Client.setId(id);
        Client.setPassword(passwordEncoder.encode(dto.getPassword())); // Garante que a senha seja atualizada e
                                                                       // codificada

        Client = ClientRepository
                .save(Client);
        return toResponseDTO(Client);
    }

    /**
     * Exclui um usuário do sistema pelo seu ID.
     *
     * @param id O UUID do usuário a ser excluído.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public void deleteById(UUID id) {
        if (!ClientRepository
                .existsById(id)) {
            throw new NoSuchException("Usuário");
        }
        ClientRepository
                .deleteById(id);
    }

    /**
     * Busca um usuário pelo seu endereço de email.
     * Este método é geralmente usado para fins internos, como autenticação.
     *
     * @param email O email do usuário a ser buscado.
     * @return A entidade {@link Client} correspondente ao email fornecido.
     * @throws NoSuchException se nenhum usuário for encontrado with o email
     *                         fornecido.
     */
    public Client findByEmail(String email) {
        return ClientRepository
                .findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private ClientResponseDTO toResponseDTO(Client Client) {
        return new ClientResponseDTO(Client);
    }

    private Client toEntity(ClientRequestDTO dto) {
        Client user = new Client();
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // A senha será codificada no método de serviço
        return user;
    }
}

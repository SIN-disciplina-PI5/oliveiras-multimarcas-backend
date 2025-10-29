package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.DTO.UserResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.repositories.UserRepositorie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepositorie userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Busca e retorna todos os usuários cadastrados.
     *
     * @return Uma lista de {@link UserResponseDTO} com os dados de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll(){
        return userRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O UUID do usuário a ser buscado.
     * @return Um {@link UserResponseDTO} com os dados do usuário encontrado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));
        return toResponseDTO(user);
    }

    /**
     * Cria um novo usuário no sistema.
     * A senha do usuário é codificada antes de ser salva.
     *
     * @param dto O {@link UserRequestDTO} com os dados do usuário a ser criado.
     * @return Um {@link UserResponseDTO} com os dados do usuário recém-criado.
     */
    @Transactional
    public UserResponseDTO insert(UserRequestDTO dto) {
        User user = toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);
        return toResponseDTO(user);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id O UUID do usuário a ser atualizado.
     * @param dto O {@link UserRequestDTO} com os novos dados do usuário.
     * @return Um {@link UserResponseDTO} com os dados do usuário atualizado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        userRepository.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        User user = toEntity(dto);
        user.setId(id);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Garante que a senha seja atualizada e codificada

        user = userRepository.save(user);
        return toResponseDTO(user);
    }

    /**
     * Exclui um usuário do sistema pelo seu ID.
     *
     * @param id O UUID do usuário a ser excluído.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public void delete(UUID id){
        if(!userRepository.existsById(id)){
            throw new NoSuchException("Usuário");
        }
        userRepository.deleteById(id);
    }

    /**
     * Busca um usuário pelo seu endereço de email.
     * Este método é geralmente usado para fins internos, como autenticação.
     *
     * @param email O email do usuário a ser buscado.
     * @return A entidade {@link User} correspondente ao email fornecido.
     * @throws NoSuchException se nenhum usuário for encontrado with o email fornecido.
     */
    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // A senha será codificada no método de serviço
        return user;
    }
}

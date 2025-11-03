package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.EmployeeRequestDTO;
import pi.oliveiras_multimarcas.DTO.EmployeeResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Employee;
import pi.oliveiras_multimarcas.repositories.EmployeeRepositorie;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepositorie EmployeeRepositorie;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Busca e retorna todos os usuários cadastrados.
     *
     * @return Uma lista de {@link EmployeeResponseDTO} com os dados de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findAll(){
        return EmployeeRepositorie.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O UUID do usuário a ser buscado.
     * @return Um {@link EmployeeResponseDTO} com os dados do usuário encontrado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID id){
        Employee employee = EmployeeRepositorie.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));
        return toResponseDTO(employee);
    }

    /**
     * Cria um novo usuário no sistema.
     * A senha do usuário é codificada antes de ser salva.
     *
     * @param dto O {@link EmployeeRequestDTO} com os dados do usuário a ser criado.
     * @return Um {@link EmployeeResponseDTO} com os dados do usuário recém-criado.
     */
    @Transactional
    public EmployeeResponseDTO insert(EmployeeRequestDTO dto) {
        Employee employee = toEntity(dto);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee = EmployeeRepositorie.save(employee);
        return toResponseDTO(employee);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id O UUID do usuário a ser atualizado.
     * @param dto O {@link EmployeeRequestDTO} com os novos dados do usuário.
     * @return Um {@link EmployeeResponseDTO} com os dados do usuário atualizado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public EmployeeResponseDTO updateById(UUID id, EmployeeRequestDTO dto) {
        EmployeeRepositorie.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Employee employee = toEntity(dto);
        employee.setId(id);
        employee.setPassword(passwordEncoder.encode(dto.getPassword())); // Garante que a senha seja atualizada e codificada

        employee = EmployeeRepositorie.save(employee);
        return toResponseDTO(employee);
    }

    /**
     * Exclui um usuário do sistema pelo seu ID.
     *
     * @param id O UUID do usuário a ser excluído.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public void deleteById(UUID id){
        if(!EmployeeRepositorie.existsById(id)){
            throw new NoSuchException("Usuário");
        }
        EmployeeRepositorie.deleteById(id);
    }

    /**
     * Busca um usuário pelo seu endereço de email.
     * Este método é geralmente usado para fins internos, como autenticação.
     *
     * @param email O email do usuário a ser buscado.
     * @return A entidade {@link Employee} correspondente ao email fornecido.
     * @throws NoSuchException se nenhum usuário for encontrado with o email fornecido.
     */
    public Employee findByEmail(String email){
        return EmployeeRepositorie.findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private EmployeeResponseDTO toResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(employee);
    }

    private Employee toEntity(EmployeeRequestDTO dto) {
        Employee user = new Employee();
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // A senha será codificada no método de serviço
        return user;
    }
}

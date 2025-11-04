package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.SaleRequestDTO;
import pi.oliveiras_multimarcas.DTO.SaleResponseDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Sale;
import pi.oliveiras_multimarcas.repositories.SaleRepositorie;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepositorie SaleRepositorie;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Busca e retorna todos os usuários cadastrados.
     *
     * @return Uma lista de {@link SaleResponseDTO} com os dados de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> findAll(){
        return SaleRepositorie.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O UUID do usuário a ser buscado.
     * @return Um {@link SaleResponseDTO} com os dados do usuário encontrado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional(readOnly = true)
    public SaleResponseDTO findById(UUID id){
        Sale sale = SaleRepositorie.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));
        return toResponseDTO(sale);
    }

    /**
     * Cria um novo usuário no sistema.
     * A senha do usuário é codificada antes de ser salva.
     *
     * @param dto O {@link SaleRequestDTO} com os dados do usuário a ser criado.
     * @return Um {@link SaleResponseDTO} com os dados do usuário recém-criado.
     */
    @Transactional
    public SaleResponseDTO insert(SaleRequestDTO dto) {
        Sale sale = toEntity(dto);
        sale.setPassword(passwordEncoder.encode(dto.getPassword()));
        sale = SaleRepositorie.save(sale);
        return toResponseDTO(sale);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id O UUID do usuário a ser atualizado.
     * @param dto O {@link SaleRequestDTO} com os novos dados do usuário.
     * @return Um {@link SaleResponseDTO} com os dados do usuário atualizado.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public SaleResponseDTO updateById(UUID id, SaleRequestDTO dto) {
        SaleRepositorie.findById(id)
                .orElseThrow(() -> new NoSuchException("Usuário"));

        Sale sale = toEntity(dto);
        sale.setId(id);
        sale.setPassword(passwordEncoder.encode(dto.getPassword())); // Garante que a senha seja atualizada e codificada

        sale = SaleRepositorie.save(sale);
        return toResponseDTO(sale);
    }

    /**
     * Exclui um usuário do sistema pelo seu ID.
     *
     * @param id O UUID do usuário a ser excluído.
     * @throws NoSuchException se nenhum usuário for encontrado com o ID fornecido.
     */
    @Transactional
    public void deleteById(UUID id){
        if(!SaleRepositorie.existsById(id)){
            throw new NoSuchException("Usuário");
        }
        SaleRepositorie.deleteById(id);
    }

    /**
     * Busca um usuário pelo seu endereço de email.
     * Este método é geralmente usado para fins internos, como autenticação.
     *
     * @param email O email do usuário a ser buscado.
     * @return A entidade {@link Sale} correspondente ao email fornecido.
     * @throws NoSuchException se nenhum usuário for encontrado with o email fornecido.
     */
    public Sale findByEmail(String email){
        return SaleRepositorie.findByEmail(email)
                .orElseThrow(() -> new NoSuchException("Usuário"));
    }

    private SaleResponseDTO toResponseDTO(Sale Sale) {
        return new SaleResponseDTO(sale);
    }

    private Sale toEntity(SaleRequestDTO dto) {
        Sale user = new Sale();
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // A senha será codificada no método de serviço
        return user;
    }
}

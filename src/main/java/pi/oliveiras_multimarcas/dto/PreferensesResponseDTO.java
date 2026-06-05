package pi.oliveiras_multimarcas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pi.oliveiras_multimarcas.models.Preferences;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreferensesResponseDTO {
    private String cnpj;
    private String urlInstagram;
    private String contact;
    private String email;
    private String address;
    private String urlAddress;

    public PreferensesResponseDTO(Preferences dto){
        cnpj = dto.getCnpj();
        urlInstagram = dto.getUrlInstagram();
        contact = dto.getContact();
        email = dto.getEmail();
        address = dto.getAddress();
        urlAddress = dto.getAddress();
    }
}

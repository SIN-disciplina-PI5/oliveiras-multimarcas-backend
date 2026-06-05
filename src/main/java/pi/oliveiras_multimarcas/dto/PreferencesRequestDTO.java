package pi.oliveiras_multimarcas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PreferencesRequestDTO {
    private String cnpj;
    private String urlInstagram;
    private String contact;
    private String email;
    private String address;
    private String urlAddress;
}

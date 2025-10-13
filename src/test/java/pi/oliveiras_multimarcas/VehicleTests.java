package pi.oliveiras_multimarcas;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import pi.oliveiras_multimarcas.DTO.VehicleRequestDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.models.Vehicle;


public class VehicleTests {

    @ParameterizedTest
    @CsvSource({
            "'Civic EXL', 2026, 125000.00, '{https://example.com/images/civic_exl_front.jpg, https://example.com/images/civic_exl_interior.jpg, https://example.com/images/civic_exl_rear.jpg}', 'Sedan médio com ótimo desempenho, conforto e baixo consumo de combustível.', 32000, 'Honda'",
            "'Corolla Altis Hybrid', 2009, 145000.00, '{https://example.com/images/corolla_altis_front.jpg, https://example.com/images/corolla_altis_side.jpg, https://example.com/images/corolla_altis_interior.jpg}', 'Versão híbrida do Corolla, unindo eficiência energética e conforto premium.', 21000, 'Toyota'",
            "'Compass Longitude', 2020, 138000.00, '{https://example.com/images/compass_longitude_front.jpg, https://example.com/images/compass_longitude_dashboard.jpg, https://example.com/images/compass_longitude_rear.jpg}', 'SUV robusto com tração 4x4 e interior sofisticado, ideal para viagens longas.', -10, 'Jeep'",
            "'Onix LTZ', 2023, 0, '{https://example.com/images/onix_ltz_front.jpg, https://example.com/images/onix_ltz_side.jpg, https://example.com/images/onix_ltz_interior.jpg}', 'Hatch compacto moderno, com excelente consumo e conectividade.', 12000, 'Chevrolet'",
            "'Polo Comfortline', 2021, -10, '{https://example.com/images/polo_front.jpg, https://example.com/images/polo_side.jpg, https://example.com/images/polo_interior.jpg}', 'Hatch médio, confortável e seguro, ideal para uso urbano.', 25000, 'Volkswagen'"
    })
    public void shouldThrowExceptionForInvalidParameters(
            String model,
            int year,
            String priceStr,
            String urlsStr,
            String description,
            int mileage,
            String mark
    ) {

        BigDecimal price = new BigDecimal(priceStr);

        // Converte urlsStr de "{url1, url2, url3}" para List<String>
        List<String> urls = List.of(urlsStr.replace("{", "").replace("}", "").split(",\\s*"));

        assertThrows(InvalidArguments.class, () -> {
            VehicleRequestDTO dto = new VehicleRequestDTO(model, year, price, urls, description, mileage, mark);
            new Vehicle(dto);
        });
    }

}
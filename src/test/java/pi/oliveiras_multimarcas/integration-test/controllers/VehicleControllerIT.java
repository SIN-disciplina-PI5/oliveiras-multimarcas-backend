package pi.oliveiras_multimarcas.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateVehicle() throws  Exception{
        String json = """
            {
                "model": "Civic EXL",
                "modelYear": 2022,
                "price": 125000.00,
                "url_images": [
                    "https://example.com/images/civic_exl_front.jpg",
                    "https://example.com/images/civic_exl_interior.jpg",
                    "https://example.com/images/civic_exl_rear.jpg"
                ],
                "description": "Sedan médio com ótimo desempenho, conforto e baixo consumo de combustível.",
                "mileage": 32000,
                "mark": "Honda"
            }
        """;

        mockMvc.perform(
            post("/vehicles/")
                    .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.model").value("Civic EXL"));
    }
}

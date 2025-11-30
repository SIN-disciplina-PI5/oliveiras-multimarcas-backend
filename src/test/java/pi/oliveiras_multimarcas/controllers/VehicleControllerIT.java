package pi.oliveiras_multimarcas.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void shouldFindAllVehicles() throws Exception{

        mockMvc.perform(
                get("/vehicles/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(1)));
    }

    @Test
    public void shouldFindVehicleById() throws Exception {
        String json = """
            {
                "model": "Corolla Altis Hybrid",
                "modelYear": 2020,
                "price": 145000.00,
                "url_images": [
                    "https://example.com/images/corolla_altis_front.jpg",
                    "https://example.com/images/corolla_altis_side.jpg",
                    "https://example.com/images/corolla_altis_interior.jpg"
                ],
                "description": "Versão híbrida do Corolla, unindo eficiência energética e conforto premium.",
                "mileage": 21000,
                "mark": "Toyota"
            }
        """;

        MvcResult result = mockMvc.perform(
                        post("/vehicles/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.read(body, "$.id"));

        mockMvc.perform(
                get("/vehicles/"+id)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.model").value("Corolla Altis Hybrid"));
    }
}

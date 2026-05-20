package pi.oliveiras_multimarcas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private DashboardSalesDTO sales;
    private DashboardVisitsDTO visits;
    private Map<String, Long> carsByBrand;
    private long totalCustomers;
}
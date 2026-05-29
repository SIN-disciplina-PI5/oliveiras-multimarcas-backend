package pi.oliveiras_multimarcas.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardProposalsDTO {
    private long totalProposals;
    private long totalAccepted;
    private long totalRejected;
    private long totalExpired;
    private long totalPending;
    private double conversionRate;
}
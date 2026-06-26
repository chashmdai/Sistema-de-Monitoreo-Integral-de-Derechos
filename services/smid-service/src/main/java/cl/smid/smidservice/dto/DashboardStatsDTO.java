package cl.smid.smidservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private long totalCasos;
    private long casosPrioritarios;
    private Map<String, Long> casosPorRegion;
    private List<String> labelsRegiones;
    private List<Long> dataRegiones;
    private List<String> labelsInstituciones;
    private List<Double> dataDesempeno;
}

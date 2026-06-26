package cl.smid.smidservice.dto;

import cl.smid.smidservice.entity.Institucion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private long alertasCriticas;
    private long oficiosPendientes;
    private long totalInstituciones;
    private DashboardStatsDTO stats;
    private List<RankingDTO> ranking;
    private Map<String, Map<String, Long>> matrizCalor;
    private Set<String> columnasDerechos;
    private List<Institucion> instituciones;
}

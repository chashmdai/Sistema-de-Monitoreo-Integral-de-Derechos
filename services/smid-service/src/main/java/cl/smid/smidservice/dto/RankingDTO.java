package cl.smid.smidservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingDTO {

    private String nombreInstitucion;
    private BigDecimal indicePromedio;
    private long cantidadCasos;
}

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
public class ComparacionDTO {

    private String nombreGarante1;
    private BigDecimal scoreGarante1;
    private String nombreGarante2;
    private BigDecimal scoreGarante2;
    private String infoComparacion;
}

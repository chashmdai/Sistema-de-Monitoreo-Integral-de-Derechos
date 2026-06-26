package cl.smid.smidservice.dto;

import cl.smid.smidservice.entity.EvaluacionGarante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaEvaluacionDTO {

    private EvaluacionGarante evaluacion;
    private boolean alertaCritica;
    private String mensaje;
}

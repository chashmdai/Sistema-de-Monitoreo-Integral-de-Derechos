package cl.smid.esnna.dto;

import java.util.List;

/**
 * Un criterio del semáforo evaluado por la IA: si se cumple y con qué evidencia.
 * 'subtipos' solo lo usa C2 (interseccionalidad); null en el resto.
 */
public record CriterioEvaluadoDTO(
        boolean cumple,
        String evidencia,
        List<String> subtipos
) {}

package cl.smid.esnna.dto;

import java.util.List;

/**
 * Resultado de guardar: el caso persistido + ids de posibles duplicados
 * detectados (advertencia, no bloqueo).
 */
public record GuardarCasoResultado(
        EsnnaDetalleDTO caso,
        List<Long> posiblesDuplicados
) {}

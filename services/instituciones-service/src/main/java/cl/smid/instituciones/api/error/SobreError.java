package cl.smid.instituciones.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Sobre de error unificado del ecosistema SMID. El campo de la ruta se llama
 * {@code ruta} (no {@code path}) y {@code detalles} es una lista (presente solo en
 * errores de validación). El {@code timestamp} es UTC en ISO-8601.
 *
 * @param status    código HTTP
 * @param error     etiqueta corta del estado HTTP
 * @param codigo    código de negocio (por ejemplo {@code INS-001}, {@code AUTZ-004})
 * @param mensaje   mensaje legible
 * @param detalles  detalles de validación por campo (puede ser nulo)
 * @param ruta      ruta de la petición
 * @param timestamp marca temporal UTC ISO-8601
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SobreError(
        int status,
        String error,
        String codigo,
        String mensaje,
        List<String> detalles,
        String ruta,
        String timestamp) {

    /**
     * Crea un sobre sin detalles.
     */
    public static SobreError de(int status, String error, String codigo, String mensaje, String ruta) {
        return new SobreError(status, error, codigo, mensaje, null, ruta, Instant.now().toString());
    }

    /**
     * Crea un sobre con detalles de validación.
     */
    public static SobreError conDetalles(int status, String error, String codigo, String mensaje,
                                         List<String> detalles, String ruta) {
        return new SobreError(status, error, codigo, mensaje, detalles, ruta, Instant.now().toString());
    }
}

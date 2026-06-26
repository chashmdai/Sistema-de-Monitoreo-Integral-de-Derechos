package cl.smid.antecedentes.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Sobre de error unificado del cluster. Campos:
 * <ul>
 *   <li>{@code codigo}: codigo estable (p. ej. {@code ANT-422}, {@code AUTZ-004}).</li>
 *   <li>{@code mensaje}: descripcion legible.</li>
 *   <li>{@code ruta}: ruta de la peticion (campo {@code ruta}, no {@code path}).</li>
 *   <li>{@code timestamp}: instante UTC en ISO-8601.</li>
 *   <li>{@code detalles}: mapa campo-&gt;mensaje, presente <strong>solo</strong> en validacion.</li>
 * </ul>
 * {@code @JsonInclude(NON_NULL)} omite {@code detalles} cuando no aplica.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SobreError(
        String codigo,
        String mensaje,
        String ruta,
        String timestamp,
        Map<String, String> detalles) {

    public static SobreError de(String codigo, String mensaje, String ruta, Map<String, String> detalles) {
        return new SobreError(codigo, mensaje, ruta, DateTimeFormatter.ISO_INSTANT.format(Instant.now()), detalles);
    }

    public static SobreError de(String codigo, String mensaje, String ruta) {
        return de(codigo, mensaje, ruta, null);
    }
}

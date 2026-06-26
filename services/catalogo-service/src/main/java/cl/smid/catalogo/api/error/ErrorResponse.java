package cl.smid.catalogo.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

/**
 * Sobre de error unificado del ecosistema (Núcleo 2.5). El frontend y los servicios
 * consumidores conmutan sobre {@code codigo} (estable), no sobre el mensaje.
 *
 * <p>Decisiones de forma (documentadas también en el README):</p>
 * <ul>
 *   <li>El nombre del campo de ruta es <b>{@code ruta}</b> (consistente con el servicio de
 *       autenticación, que es contrato congelado).</li>
 *   <li>{@code error} es la <b>frase de estado HTTP</b> (p. ej. «Unprocessable Entity»,
 *       «Conflict»), igual que en auth.</li>
 *   <li>{@code detalles} (mapa campo→mensaje) solo aparece en errores de validación; gracias a
 *       {@link JsonInclude}, cuando es nulo no se serializa.</li>
 *   <li>{@code timestamp} es un {@link Instant} en UTC (ISO-8601), por la configuración global
 *       de Jackson ({@code write-dates-as-timestamps: false}).</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Sobre de error unificado del ecosistema SMID.")
public record ErrorResponse(
        @Schema(description = "Estado HTTP numerico.", example = "404")
        int status,
        @Schema(description = "Frase HTTP estandar.", example = "Not Found")
        String error,
        @Schema(description = "Codigo estable de error.", allowableValues = {"AUTZ-003", "AUTZ-004", "CAT-001", "CAT-002", "CAT-003", "CAT-004", "CAT-005", "CAT-006", "CAT-500"}, example = "CAT-001")
        String codigo,
        @Schema(description = "Mensaje legible.", example = "El derecho solicitado no existe.")
        String mensaje,
        @Schema(description = "Detalles campo-mensaje solo en errores de validacion.", nullable = true)
        Map<String, String> detalles,
        @Schema(description = "Ruta interna que produjo el error.", example = "/catalogo/derechos/abc")
        String ruta,
        @Schema(description = "Instante UTC del error.", example = "2026-06-13T12:34:56.789Z")
        Instant timestamp
) {

    /**
     * Construye el sobre a partir de un {@link CodigoError}, resolviendo el estado HTTP y su
     * frase descriptiva.
     *
     * @param codigoError código estable del catálogo de errores
     * @param mensaje     mensaje legible (puede ser el por defecto del código o uno puntual)
     * @param ruta        URI de la solicitud
     * @param detalles    mapa campo→mensaje (solo validación; {@code null} en otros casos)
     * @param momento     instante del error (UTC)
     */
    public static ErrorResponse de(CodigoError codigoError, String mensaje, String ruta,
                                   Map<String, String> detalles, Instant momento) {
        HttpStatus estado = HttpStatus.valueOf(codigoError.getHttpStatus());
        return new ErrorResponse(
                estado.value(),
                estado.getReasonPhrase(),
                codigoError.getCodigo(),
                mensaje,
                detalles,
                ruta,
                momento);
    }
}

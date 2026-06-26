package cl.smid.esnna.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Reemplaza los String "SÍ"/"NO" sueltos del modelo original.
 *
 * - En BD se persiste el nombre del enum (SI/NO) vía @Enumerated(STRING).
 * - En JSON se expone como "SÍ"/"NO" (etiqueta), por compatibilidad con el front.
 * - La deserialización es tolerante (SÍ, SI, Si, si, S, true, 1...), resolviendo
 *   la fragilidad del @Pattern con tilde del DTO original (VAL-2).
 */
public enum RespuestaSiNo {
    SI("SÍ"),
    NO("NO");

    private final String etiqueta;

    RespuestaSiNo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static RespuestaSiNo desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String v = valor.trim().toUpperCase(Locale.ROOT).replace("Í", "I");
        return switch (v) {
            case "SI", "S", "TRUE", "1"  -> SI;
            case "NO", "N", "FALSE", "0" -> NO;
            default -> null;
        };
    }
}

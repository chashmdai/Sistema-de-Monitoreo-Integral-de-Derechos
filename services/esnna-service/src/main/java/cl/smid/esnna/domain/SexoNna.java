package cl.smid.esnna.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Sexo del NNA. X cubre intersexual / no informado / no binario, evitando
 * perder el dato cuando el documento no lo precisa como M o F.
 */
public enum SexoNna {
    M, F, X;

    @JsonValue
    public String valor() {
        return name();
    }

    @JsonCreator
    public static SexoNna desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String v = valor.trim().toUpperCase(Locale.ROOT);
        return switch (v) {
            case "M", "MASCULINO", "HOMBRE", "VARON", "VARÓN" -> M;
            case "F", "FEMENINO", "MUJER" -> F;
            default -> X;
        };
    }
}

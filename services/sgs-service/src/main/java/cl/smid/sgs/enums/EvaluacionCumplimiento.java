package cl.smid.sgs.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Veredicto cualitativo de cumplimiento (rúbrica metodológica DDN).
 * El label es el contrato textual usado en el prompt y en el Excel; el nombre del enum es estable.
 */
public enum EvaluacionCumplimiento {
    CUMPLIMIENTO_TOTAL("Cumplimiento Total"),
    CUMPLIMIENTO_PARCIAL_SUSTANCIAL("Cumplimiento Parcial Sustancial"),
    CUMPLIMIENTO_PARCIAL("Cumplimiento Parcial"),
    INCUMPLIMIENTO("Incumplimiento"),
    SIN_INFORMACION("No hay información"),
    NO_APLICA("No aplica");

    private final String label;
    EvaluacionCumplimiento(String label) { this.label = label; }

    @JsonValue
    public String getLabel() { return label; }

    @JsonCreator
    public static EvaluacionCumplimiento from(String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim();
        for (EvaluacionCumplimiento e : values()) {
            if (e.label.equalsIgnoreCase(v) || e.name().equalsIgnoreCase(v)) return e;
        }
        // tolerancia a variantes sin tilde
        String norm = v.toLowerCase().replace("informacion", "información");
        for (EvaluacionCumplimiento e : values()) {
            if (e.label.equalsIgnoreCase(norm)) return e;
        }
        throw new IllegalArgumentException("Valor de evaluación de cumplimiento inválido: " + value);
    }
}

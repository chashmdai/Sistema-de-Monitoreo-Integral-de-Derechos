package cl.smid.sgs.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.Period;

/**
 * Fase de seguimiento institucional. Determina el "reloj 2" (alerta de estancamiento interno).
 * ADMINISTRATIVO es transversal y no tiene offset de vencimiento.
 */
public enum FaseSeguimiento {
    F1_REGISTRO("Fase 1 - Registro", Period.ofMonths(1)),
    F2_ANALISIS("Fase 2 - Análisis Preliminar", Period.ofMonths(6)),
    F3_EVALUACION_VISITA("Fase 3 - Evaluación y Visita de Seguimiento", Period.ofMonths(9)),
    F4_NUEVAS_RECOMENDACIONES("Fase 4 - Seguimiento de Nuevas Recomendaciones", Period.ofMonths(12)),
    ADMINISTRATIVO("Seguimiento Administrativo", null);

    private final String label;
    private final Period offset;

    FaseSeguimiento(String label, Period offset) { this.label = label; this.offset = offset; }

    @JsonValue
    public String getLabel() { return label; }

    /** Puede ser null (ADMINISTRATIVO): en ese caso no genera alerta de estancamiento. */
    public Period offset() { return offset; }

    @JsonCreator
    public static FaseSeguimiento from(String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim();
        for (FaseSeguimiento f : values()) {
            if (f.label.equalsIgnoreCase(v) || f.name().equalsIgnoreCase(v)) return f;
        }
        throw new IllegalArgumentException("Valor de fase inválido: " + value);
    }
}

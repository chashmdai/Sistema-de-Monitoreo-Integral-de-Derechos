package cl.smid.sgs.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.Period;

/**
 * Plazo otorgado al destinatario. Determina el "reloj 1" (alerta al destinatario).
 * Regla de corte por días confirmada (decisión #10): la IA emite días/fechas y el backend mapea.
 */
public enum PlazoRecomendacion {
    URGENTE("Urgente", 7, Period.ofWeeks(1)),
    CORTO_PLAZO("Corto Plazo", 30, Period.ofMonths(1)),
    MEDIANO_PLAZO("Mediano Plazo", 180, Period.ofMonths(6)),
    LARGO_PLAZO("Largo Plazo", Integer.MAX_VALUE, Period.ofYears(1));

    private final String label;
    private final int diasTope;
    private final Period duracion;

    PlazoRecomendacion(String label, int diasTope, Period duracion) {
        this.label = label; this.diasTope = diasTope; this.duracion = duracion;
    }

    @JsonValue
    public String getLabel() { return label; }

    /** Periodo calendario para computar la fecha límite al destinatario. */
    public Period duracion() { return duracion; }

    /** Mapea una cantidad de días al tramo de plazo (regla de corte §3.1). */
    public static PlazoRecomendacion fromDias(int dias) {
        if (dias <= URGENTE.diasTope) return URGENTE;
        if (dias <= CORTO_PLAZO.diasTope) return CORTO_PLAZO;
        if (dias <= MEDIANO_PLAZO.diasTope) return MEDIANO_PLAZO;
        return LARGO_PLAZO;
    }

    @JsonCreator
    public static PlazoRecomendacion from(String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim();
        for (PlazoRecomendacion p : values()) {
            if (p.label.equalsIgnoreCase(v) || p.name().equalsIgnoreCase(v)) return p;
        }
        throw new IllegalArgumentException("Valor de plazo inválido: " + value);
    }
}

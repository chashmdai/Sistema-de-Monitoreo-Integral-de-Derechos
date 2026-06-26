package cl.smid.sgs.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Ciclo de vida de la gestión de una recomendación (reemplaza el String libre "PENDIENTE").
 * Transiciones validadas en backend; acoge=NO desvía a la rama de insistencia (§3.1).
 */
public enum EstadoGestion {
    PENDIENTE_REGISTRO,
    EN_SEGUIMIENTO,
    NO_ACOGIDA,
    EN_INSISTENCIA,
    CERRADA_CUMPLIDA,
    CERRADA_INCUMPLIDA,
    ANULADA;

    private static final Map<EstadoGestion, Set<EstadoGestion>> TRANSICIONES = new EnumMap<>(EstadoGestion.class);
    static {
        TRANSICIONES.put(PENDIENTE_REGISTRO, EnumSet.of(EN_SEGUIMIENTO, NO_ACOGIDA, ANULADA));
        TRANSICIONES.put(EN_SEGUIMIENTO,     EnumSet.of(CERRADA_CUMPLIDA, CERRADA_INCUMPLIDA, EN_INSISTENCIA, ANULADA));
        TRANSICIONES.put(NO_ACOGIDA,         EnumSet.of(EN_INSISTENCIA, CERRADA_INCUMPLIDA, ANULADA));
        TRANSICIONES.put(EN_INSISTENCIA,     EnumSet.of(CERRADA_CUMPLIDA, CERRADA_INCUMPLIDA, ANULADA));
        TRANSICIONES.put(CERRADA_CUMPLIDA,   EnumSet.of(ANULADA));
        TRANSICIONES.put(CERRADA_INCUMPLIDA, EnumSet.of(EN_SEGUIMIENTO, ANULADA)); // reapertura opcional
        TRANSICIONES.put(ANULADA,            EnumSet.noneOf(EstadoGestion.class));
    }

    public boolean puedeTransicionarA(EstadoGestion destino) {
        return destino != null && TRANSICIONES.getOrDefault(this, EnumSet.noneOf(EstadoGestion.class)).contains(destino);
    }

    public boolean esTerminal() {
        return this == ANULADA;
    }
}

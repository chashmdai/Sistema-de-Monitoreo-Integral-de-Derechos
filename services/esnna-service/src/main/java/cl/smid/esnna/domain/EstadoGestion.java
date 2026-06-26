package cl.smid.esnna.domain;

import java.util.EnumSet;
import java.util.Set;

/**
 * Estado del ciclo de vida del caso. Antes era un String que nacía en
 * PENDIENTE_REVISION y nunca cambiaba (enum muerto). Ahora es una máquina de
 * estados con transiciones validadas: el endpoint de transición rechaza saltos
 * ilegales (p.ej. PENDIENTE_REVISION -> CERRADO directo).
 *
 * ANULADO es el borrado lógico: alcanzable desde cualquier estado, terminal,
 * y exige motivo + autor a nivel de servicio.
 */
public enum EstadoGestion {
    PENDIENTE_REVISION,
    EN_REVISION,
    EN_QUERELLA,
    DERIVADO,
    CERRADO,
    ANULADO;

    private Set<EstadoGestion> destinos() {
        return switch (this) {
            case PENDIENTE_REVISION -> EnumSet.of(EN_REVISION, ANULADO);
            case EN_REVISION        -> EnumSet.of(EN_QUERELLA, DERIVADO, CERRADO, ANULADO);
            case EN_QUERELLA        -> EnumSet.of(DERIVADO, CERRADO, ANULADO);
            case DERIVADO           -> EnumSet.of(CERRADO, ANULADO);
            case CERRADO            -> EnumSet.of(EN_REVISION, ANULADO); // reapertura
            case ANULADO            -> EnumSet.noneOf(EstadoGestion.class); // terminal
        };
    }

    public boolean puedeTransicionarA(EstadoGestion destino) {
        return destino != null && destinos().contains(destino);
    }
}

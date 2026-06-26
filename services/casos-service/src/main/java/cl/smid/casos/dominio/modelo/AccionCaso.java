package cl.smid.casos.dominio.modelo;

/**
 * Acciones (eventos de transición) que un usuario puede aplicar sobre un Caso.
 *
 * <p>La máquina de estados ({@code MaquinaEstadosCaso}) mapea pares
 * {@code (EstadoCaso origen, AccionCaso accion) → EstadoCaso destino}. Una acción no contemplada
 * para el estado actual produce una transición inválida (CAS-409).</p>
 *
 * <p>Algunas acciones se consideran <strong>administrativas</strong> y exigen rol de Coordinación
 * (ver {@link #esAdministrativa()}); el resto son operativas y solo requieren alcance territorial.</p>
 */
public enum AccionCaso {
    /** ABIERTO → EN_INVESTIGACION. Operativa. */
    INICIAR_INVESTIGACION,
    /** EN_INVESTIGACION → EN_SEGUIMIENTO. Operativa. */
    DERIVAR_A_SEGUIMIENTO,
    /** EN_INVESTIGACION | EN_SEGUIMIENTO → SUSPENDIDO. Operativa. */
    SUSPENDER,
    /** SUSPENDIDO | EN_SEGUIMIENTO → EN_INVESTIGACION. Operativa. */
    REANUDAR,
    /** ABIERTO | EN_INVESTIGACION | EN_SEGUIMIENTO | SUSPENDIDO → CERRADO. Administrativa. */
    CERRAR,
    /** CERRADO → EN_INVESTIGACION. Administrativa. */
    REABRIR,
    /** CERRADO → ARCHIVADO (terminal). Administrativa. */
    ARCHIVAR;

    /**
     * Indica si la acción exige rol de Coordinación. El cierre, la reapertura y el archivo son
     * decisiones con peso institucional sobre el expediente y se reservan a Coordinación.
     */
    public boolean esAdministrativa() {
        return this == CERRAR || this == REABRIR || this == ARCHIVAR;
    }
}

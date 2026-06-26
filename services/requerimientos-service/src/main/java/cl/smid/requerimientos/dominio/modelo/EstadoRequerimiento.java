package cl.smid.requerimientos.dominio.modelo;

/**
 * Estados posibles de un requerimiento (Núcleo 6.3).
 *
 * <p>La máquina de estados es: {@code BORRADOR -> INGRESADO -> EN_ADMISIBILIDAD ->
 * { INADMISIBLE | RESPONDIDO | ASIGNADO }}. La ventana de mutabilidad (edición
 * permitida) abarca {@code BORRADOR}, {@code INGRESADO} y {@code EN_ADMISIBILIDAD};
 * al pasar a {@code ASIGNADO} el requerimiento se cierra a edición.</p>
 *
 * <p>Pertenece al dominio puro: sin anotaciones de framework.</p>
 */
public enum EstadoRequerimiento {

    /** Recién creado; admite guardado parcial de la mayoría de los campos. */
    BORRADOR,

    /** Enviado (USR.01); ya cumple los mínimos (canal, sede y al menos un NNA). */
    INGRESADO,

    /** Abierto para la decisión de admisibilidad (USR.02). */
    EN_ADMISIBILIDAD,

    /** Decisión: fuera de competencia; opcionalmente escalado a la Defensora. */
    INADMISIBLE,

    /** Decisión: respuesta inmediata. Registra y NO envía comunicación alguna. */
    RESPONDIDO,

    /** Decisión: asignado a un profesional. Semilla de Caso (6.4); cierra a edición. */
    ASIGNADO;

    /**
     * Indica si un requerimiento en este estado es editable (ventana de mutabilidad).
     *
     * @return {@code true} si admite edición ({@code BORRADOR}/{@code INGRESADO}/
     *         {@code EN_ADMISIBILIDAD}); {@code false} en los estados terminales.
     */
    public boolean esMutable() {
        return this == BORRADOR || this == INGRESADO || this == EN_ADMISIBILIDAD;
    }

    /**
     * Indica si este estado es terminal (no admite más transiciones).
     *
     * @return {@code true} para {@code INADMISIBLE}, {@code RESPONDIDO} y {@code ASIGNADO}.
     */
    public boolean esTerminal() {
        return this == INADMISIBLE || this == RESPONDIDO || this == ASIGNADO;
    }
}

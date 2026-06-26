package cl.smid.requerimientos.dominio.servicio;

/**
 * Eventos que disparan una transición en la máquina de estados del requerimiento
 * (Núcleo 6.3). Modelar las transiciones como pares (estado, evento) -> estado permite
 * una tabla pura, sin condicionales dispersos por el código.
 */
public enum EventoTransicion {

    /** El requerimiento se envía: BORRADOR -> INGRESADO (exige mínimos: canal, sede, NNA). */
    ENVIAR,

    /** Se abre la admisibilidad: INGRESADO -> EN_ADMISIBILIDAD. */
    ABRIR_ADMISIBILIDAD,

    /** Decisión de admisibilidad INADMISIBLE: EN_ADMISIBILIDAD -> INADMISIBLE. */
    DECIDIR_INADMISIBLE,

    /** Decisión de admisibilidad RESPUESTA_INMEDIATA: EN_ADMISIBILIDAD -> RESPONDIDO. */
    DECIDIR_RESPUESTA_INMEDIATA,

    /** Decisión de admisibilidad ASIGNACION: EN_ADMISIBILIDAD -> ASIGNADO. */
    DECIDIR_ASIGNACION
}

package cl.smid.requerimientos.dominio.modelo;

/**
 * Acción de la decisión de admisibilidad (USR.02, Núcleo 6.5). Exactamente una por
 * decisión; las tres son disjuntas.
 */
public enum AccionAdmisibilidad {

    /** Fuera de competencia. Lleva a estado {@code INADMISIBLE}; admite escalada a la Defensora. */
    INADMISIBLE,

    /**
     * Respuesta inmediata. Lleva a estado {@code RESPONDIDO}. <b>Registra y NO envía</b>
     * ninguna comunicación saliente (comportamiento heredado explícito).
     */
    RESPUESTA_INMEDIATA,

    /**
     * Asignación a un profesional responsable. Exige {@code idProfesionalAsignadoAlt};
     * lleva a estado {@code ASIGNADO}, cierra a edición y emite {@code requerimiento.asignado}.
     */
    ASIGNACION
}

package cl.smid.sgs.enums;

/** Los dos relojes de vencimiento (decisión #4). */
public enum TipoAlerta {
    PLAZO_DESTINATARIO,   // Oficio.fechaIngreso + plazo.duracion
    ESTANCAMIENTO_FASE    // hito de fase + fase.offset
}

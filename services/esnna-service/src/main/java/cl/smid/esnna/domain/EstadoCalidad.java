package cl.smid.esnna.domain;

/**
 * Calidad de la consolidación, para que una priorización construida sobre
 * documentos incompletos NO se presente igual que una completa (BIZ-1/BIZ-2).
 */
public enum EstadoCalidad {
    COMPLETA,   // todos los documentos aportaron al consolidado
    PARCIAL     // hubo documentos omitidos (PDF ilegible o fallo de IA)
}

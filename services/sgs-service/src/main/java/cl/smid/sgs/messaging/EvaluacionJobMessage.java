package cl.smid.sgs.messaging;

import java.util.List;

/** Mensaje de Fase B. ids = candidatas a evaluar; el PDF de respuesta se recupera por hash. */
public record EvaluacionJobMessage(String jobId, String pdfHash, String nombreArchivo, List<Long> ids) {}

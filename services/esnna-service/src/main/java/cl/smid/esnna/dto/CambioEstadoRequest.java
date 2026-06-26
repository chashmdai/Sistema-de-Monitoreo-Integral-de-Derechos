package cl.smid.esnna.dto;

import cl.smid.esnna.domain.EstadoGestion;
import jakarta.validation.constraints.NotNull;

/**
 * Transición de estado. 'motivo' es obligatorio para ANULADO (borrado lógico),
 * validado en el servicio.
 */
public record CambioEstadoRequest(
        @NotNull(message = "estadoDestino es obligatorio")
        EstadoGestion estadoDestino,
        String motivo
) {}

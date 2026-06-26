package cl.smid.sgs.dto.in;

import cl.smid.sgs.enums.EstadoGestion;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoDTO(@NotNull EstadoGestion nuevoEstado, String motivo) {}

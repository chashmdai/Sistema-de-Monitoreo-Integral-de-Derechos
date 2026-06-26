package cl.smid.antecedentes.api.dto;

import jakarta.validation.constraints.Size;

/**
 * Cuerpo opcional de las acciones de revision (devolver/aprobar/rechazar/enviar-revision): puede
 * incluir una {@code observacion} que queda en el historial. La accion se infiere del endpoint.
 */
public record AccionRevisionRequest(@Size(max = 1000) String observacion) {
}

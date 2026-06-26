package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo para cambiar el estado de un hallazgo. El destino debe ser {@code ASOCIADO} o
 * {@code RECHAZADO} (lo valida el dominio).
 */
public record CambiarEstadoHallazgoRequest(@NotNull EstadoHallazgo estado) {

    public GestionHallazgosUseCase.ComandoCambiarEstado aComando() {
        return new GestionHallazgosUseCase.ComandoCambiarEstado(estado);
    }
}

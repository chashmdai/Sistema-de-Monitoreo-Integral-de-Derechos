package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Cuerpo para editar una propuesta de hallazgo (solo en estado PROPUESTO).
 */
public record EditarHallazgoRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank @Size(max = 4000) String descripcion,
        @NotBlank @Size(max = 36) String instrumentoId,
        @NotNull Temporalidad temporalidad,
        List<@NotBlank @Size(max = 36) String> unidadesInvolucradas,
        List<@NotBlank @Size(max = 36) String> institucionesExternas) {

    public GestionHallazgosUseCase.ComandoEditar aComando() {
        return new GestionHallazgosUseCase.ComandoEditar(titulo, descripcion, instrumentoId, temporalidad,
                unidadesInvolucradas, institucionesExternas);
    }
}

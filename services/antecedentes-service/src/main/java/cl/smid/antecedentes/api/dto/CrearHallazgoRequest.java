package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Cuerpo para crear una propuesta de hallazgo directa (rol gestion/revisor). {@code instrumentoId}
 * transporta el alt_key del instrumento.
 */
public record CrearHallazgoRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank @Size(max = 4000) String descripcion,
        @NotBlank @Size(max = 36) String instrumentoId,
        @NotNull Temporalidad temporalidad,
        List<@NotBlank @Size(max = 36) String> unidadesInvolucradas,
        List<@NotBlank @Size(max = 36) String> institucionesExternas) {

    public GestionHallazgosUseCase.ComandoCrear aComando() {
        return new GestionHallazgosUseCase.ComandoCrear(titulo, descripcion, instrumentoId, temporalidad,
                unidadesInvolucradas, institucionesExternas);
    }
}

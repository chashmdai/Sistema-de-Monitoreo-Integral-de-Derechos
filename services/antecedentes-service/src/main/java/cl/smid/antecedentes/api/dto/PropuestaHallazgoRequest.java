package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Datos del hallazgo a proponer cuando la ficha declara {@code SE_PROPONE_HALLAZGO}.
 * {@code instrumentoId} transporta el alt_key del instrumento.
 */
public record PropuestaHallazgoRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank @Size(max = 4000) String descripcion,
        @NotBlank @Size(max = 36) String instrumentoId,
        @NotNull Temporalidad temporalidad,
        List<@NotBlank @Size(max = 36) String> unidadesInvolucradas,
        List<@NotBlank @Size(max = 36) String> institucionesExternas) {

    public GestionFichasUseCase.DatosPropuestaHallazgo aDatos() {
        return new GestionFichasUseCase.DatosPropuestaHallazgo(
                titulo, descripcion, instrumentoId, temporalidad, unidadesInvolucradas, institucionesExternas);
    }
}

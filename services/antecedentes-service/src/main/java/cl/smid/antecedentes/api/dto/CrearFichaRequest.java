package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * Cuerpo para crear una ficha. Los identificadores de referencia transportan alt_key (UUID).
 * Para {@code SE_PROPONE_HALLAZGO} debe venir {@code propuestaHallazgo}; para
 * {@code ANTECEDENTE_DE_HALLAZGO}, {@code hallazgoAlt}; para {@code NO_ES_HALLAZGO}, ninguno
 * (la coherencia la valida el dominio).
 */
public record CrearFichaRequest(
        @NotBlank @Size(max = 36) String procesoId,
        @Size(max = 36) String casoAlt,
        @NotBlank @Size(max = 36) String categoriaPrincipalId,
        @Size(max = 2, message = "Se admiten como maximo 2 categorias secundarias")
        List<@NotBlank @Size(max = 36) String> categoriasSecundariasIds,
        List<@Min(1) @Max(54) Integer> derechosCdn,
        @NotBlank @Size(max = 4000) String descripcion,
        @NotBlank String relato,
        @NotNull Calificacion calificacion,
        Set<Criterio> criterios,
        @NotNull PercepcionHallazgo percepcionHallazgo,
        @Size(max = 36) String jefaturaAlt,
        @Size(max = 36) String hallazgoAlt,
        @Valid PropuestaHallazgoRequest propuestaHallazgo) {

    public GestionFichasUseCase.ComandoCrear aComando() {
        return new GestionFichasUseCase.ComandoCrear(procesoId, casoAlt, categoriaPrincipalId,
                categoriasSecundariasIds, derechosCdn, descripcion, relato, calificacion, criterios,
                percepcionHallazgo, jefaturaAlt, hallazgoAlt,
                propuestaHallazgo == null ? null : propuestaHallazgo.aDatos());
    }
}

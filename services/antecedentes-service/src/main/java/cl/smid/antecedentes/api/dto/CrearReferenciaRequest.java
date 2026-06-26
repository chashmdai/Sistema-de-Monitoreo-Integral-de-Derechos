package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para crear una referencia (categoria/proceso/instrumento). El {@code codigo} es unico e
 * inmutable.
 */
public record CrearReferenciaRequest(
        @NotBlank @Size(max = 80) String codigo,
        @NotBlank @Size(max = 200) String nombre) {

    public GestionReferenciasUseCase.ComandoCrear aComando() {
        return new GestionReferenciasUseCase.ComandoCrear(codigo, nombre);
    }
}

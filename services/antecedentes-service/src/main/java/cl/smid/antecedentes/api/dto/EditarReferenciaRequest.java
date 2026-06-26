package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para editar el nombre de una referencia (el codigo es inmutable).
 */
public record EditarReferenciaRequest(@NotBlank @Size(max = 200) String nombre) {

    public GestionReferenciasUseCase.ComandoEditar aComando() {
        return new GestionReferenciasUseCase.ComandoEditar(nombre);
    }
}

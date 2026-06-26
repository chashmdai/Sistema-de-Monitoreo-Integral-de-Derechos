package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo para activar/desactivar una referencia.
 */
public record VigenciaReferenciaRequest(@NotNull Boolean vigente) {

    public GestionReferenciasUseCase.ComandoVigencia aComando() {
        return new GestionReferenciasUseCase.ComandoVigencia(Boolean.TRUE.equals(vigente));
    }
}

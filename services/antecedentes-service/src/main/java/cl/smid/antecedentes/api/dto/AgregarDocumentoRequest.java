package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para agregar el metadato de un documento a una ficha. Sin binario; la
 * {@code referenciaExterna} se poblara cuando exista Documentos (6.9).
 */
public record AgregarDocumentoRequest(
        @NotBlank @Size(max = 255) String nombre,
        @Size(max = 255) String referenciaExterna) {

    public GestionFichasUseCase.ComandoAgregarDocumento aComando() {
        return new GestionFichasUseCase.ComandoAgregarDocumento(nombre, referenciaExterna);
    }
}

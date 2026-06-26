package cl.smid.antecedentes.dominio.modelo;

/**
 * Metadato de un documento asociado a una {@link FichaAntecedente}. En este servicio se
 * guardan <strong>solo metadatos</strong>: la {@code referenciaExterna} al binario queda
 * nula hasta que exista el servicio de Documentos (6.9), que poblara el vinculo.
 *
 * @param altKey            identificador publico opaco del documento
 * @param nombre            nombre legible del documento
 * @param referenciaExterna referencia al binario en Documentos (6.9); puede ser nula
 */
public record DocumentoAsociado(
        String altKey,
        String nombre,
        String referenciaExterna) {

    public DocumentoAsociado {
        if (altKey == null || altKey.isBlank()) {
            throw new IllegalArgumentException("El alt_key del documento es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del documento es obligatorio");
        }
    }
}

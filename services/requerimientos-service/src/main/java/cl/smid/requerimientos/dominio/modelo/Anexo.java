package cl.smid.requerimientos.dominio.modelo;

import java.util.Objects;

/**
 * Metadatos de un anexo del requerimiento (Núcleo 6.5). <b>Solo metadatos</b>: el binario
 * llega con el servicio de Documentos (6.9), que poblará {@code referenciaExterna}. Hasta
 * entonces ese campo es nulo. Aquí no hay manejo de binarios.
 *
 * @param idInterno          handle de persistencia (nulo si aún no persistido)
 * @param nombreArchivo      nombre original del archivo
 * @param tipoMime           tipo MIME declarado (nulable)
 * @param tamanoBytes        tamaño en bytes (nulable)
 * @param referenciaExterna  referencia al binario en Documentos (nula hasta 6.9)
 */
public record Anexo(
        Long idInterno,
        String nombreArchivo,
        String tipoMime,
        Long tamanoBytes,
        String referenciaExterna) {

    public Anexo {
        Objects.requireNonNull(nombreArchivo, "El nombre de archivo es obligatorio");
        if (nombreArchivo.isBlank()) {
            throw new IllegalArgumentException("El nombre de archivo no puede estar en blanco");
        }
    }

    /**
     * Crea un anexo nuevo solo con metadatos (sin referencia al binario aún).
     *
     * @param nombreArchivo nombre del archivo
     * @param tipoMime      tipo MIME (nulable)
     * @param tamanoBytes   tamaño (nulable)
     * @return el anexo
     */
    public static Anexo metadatos(String nombreArchivo, String tipoMime, Long tamanoBytes) {
        return new Anexo(null, nombreArchivo, tipoMime, tamanoBytes, null);
    }
}

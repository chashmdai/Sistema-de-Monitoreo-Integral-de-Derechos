package cl.smid.requerimientos.dominio.modelo;

import java.util.Objects;

/**
 * Derecho vulnerado (y su causa opcional) imputado a un NNA dentro de un requerimiento.
 *
 * <p>Solo guarda referencias por {@code alt_key} hacia catalogo-service (6.7); ningún
 * servicio conoce las llaves internas de otro. {@code idInterno} es un identificador de
 * persistencia opaco (nulo para líneas nuevas) que jamás cruza la API.</p>
 *
 * @param idInterno     handle de persistencia (nulo si la línea aún no se ha persistido)
 * @param idDerechoAlt  alt_key del derecho en el Catálogo (obligatorio)
 * @param idCausaAlt    alt_key de la causa en el Catálogo (opcional)
 */
public record DerechoVulnerado(Long idInterno, String idDerechoAlt, String idCausaAlt) {

    public DerechoVulnerado {
        Objects.requireNonNull(idDerechoAlt, "El alt_key del derecho es obligatorio");
        if (idDerechoAlt.isBlank()) {
            throw new IllegalArgumentException("El alt_key del derecho no puede estar en blanco");
        }
    }

    /**
     * Crea una línea nueva (sin persistir).
     *
     * @param idDerechoAlt alt_key del derecho
     * @param idCausaAlt   alt_key de la causa (nulable)
     * @return la línea de derecho vulnerado
     */
    public static DerechoVulnerado nuevo(String idDerechoAlt, String idCausaAlt) {
        return new DerechoVulnerado(null, idDerechoAlt, idCausaAlt);
    }

    /** @return {@code true} si la línea aún no se ha persistido. */
    public boolean esNuevo() {
        return idInterno == null;
    }
}

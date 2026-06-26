package cl.smid.requerimientos.dominio.puerto.salida;

/**
 * Puerto de salida que genera identificadores públicos (alt_key, UUID v4) para los nuevos
 * requerimientos. El dominio nunca expone la PK interna (cierre IDOR, Núcleo 2.2).
 */
public interface GeneradorAltKey {

    /** @return un nuevo alt_key (UUID) en formato canónico de 36 caracteres. */
    String nuevo();
}

package cl.smid.antecedentes.dominio.modelo;

import java.time.Instant;

/**
 * Asiento del historial de una {@link FichaAntecedente}. Registra cada hito del ciclo
 * de vida (creacion, envio a revision, devolucion, aprobacion, rechazo) con el actor y
 * el instante en que ocurrio. Es inmutable.
 *
 * @param tipoEvento etiqueta del hito (p. ej. {@code CREACION}, {@code ENVIO_REVISION})
 * @param actorAlt   alt_key del usuario que ejecuto la accion
 * @param ocurridoEn instante UTC del hito
 * @param observacion observacion opcional asociada al hito
 */
public record AsientoHistorial(
        String tipoEvento,
        String actorAlt,
        Instant ocurridoEn,
        String observacion) {

    public AsientoHistorial {
        if (tipoEvento == null || tipoEvento.isBlank()) {
            throw new IllegalArgumentException("El tipo de evento del historial es obligatorio");
        }
        if (ocurridoEn == null) {
            throw new IllegalArgumentException("El instante del asiento de historial es obligatorio");
        }
    }
}

package cl.smid.casos.dominio.modelo;

import java.time.Instant;
import java.util.Objects;

/**
 * Asiento inmutable del historial de transiciones de un Caso. Cada cambio de estado (incluida la
 * materialización inicial) deja un registro con su actor y su instante.
 *
 * <p>El campo {@link #accion()} es {@code String} —no {@link AccionCaso}— porque el asiento de
 * apertura usa la pseudo-acción {@code "MATERIALIZACION"} (génesis del expediente desde el evento),
 * que no es una acción de usuario gobernada por la máquina de estados.</p>
 *
 * @param altKey        identificador opaco del asiento.
 * @param estadoOrigen  estado previo (nulo en la materialización).
 * @param estadoDestino estado resultante.
 * @param accion        nombre de la acción aplicada ({@code AccionCaso.name()} o {@code "MATERIALIZACION"}).
 * @param observacion   nota opcional asociada a la transición.
 * @param actorAlt      alt_key del usuario que la ejecutó (o el actor de sistema en la materialización).
 * @param ocurridoEn    instante UTC de la transición.
 */
public record Transicion(String altKey, EstadoCaso estadoOrigen, EstadoCaso estadoDestino,
                         String accion, String observacion, String actorAlt, Instant ocurridoEn) {

    /** Pseudo-acción reservada para el asiento de apertura del expediente. */
    public static final String ACCION_MATERIALIZACION = "MATERIALIZACION";

    public Transicion {
        Objects.requireNonNull(altKey, "altKey");
        Objects.requireNonNull(estadoDestino, "estadoDestino");
        Objects.requireNonNull(accion, "accion");
        Objects.requireNonNull(ocurridoEn, "ocurridoEn");
    }
}

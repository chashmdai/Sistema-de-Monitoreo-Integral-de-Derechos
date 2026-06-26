package cl.smid.requerimientos.dominio.modelo;

import java.time.Instant;

/**
 * Copia local y acotada de datos de una persona de personas-service, tomada al momento
 * del ingreso o de la asociación (Núcleo 6.2, patrón de snapshots denormalizados).
 *
 * <p>Permite mostrar el requerimiento <b>sin llamar a Personas en cada lectura</b>. Solo
 * guarda lo mínimo (nombre legible y RUT); el vínculo persistente real es el {@code alt_key}
 * de la persona. Una reconciliación periódica refresca estos snapshots.</p>
 *
 * <p>Nota de protección de datos: el snapshot es deliberadamente mínimo; no copia el
 * registro completo de la persona.</p>
 *
 * @param nombreLegible nombre para mostrar (puede ser {@code null} si Personas no lo expone)
 * @param rut           RUT normalizado (puede ser {@code null}; un NNA puede no tener RUT)
 * @param capturadoEn   instante UTC en que se tomó el snapshot
 */
public record SnapshotPersona(String nombreLegible, String rut, Instant capturadoEn) {

    /**
     * Crea un snapshot marcando el instante de captura.
     *
     * @param nombreLegible nombre para mostrar
     * @param rut           RUT (nulable)
     * @param momento       instante de captura (UTC)
     * @return el snapshot
     */
    public static SnapshotPersona de(String nombreLegible, String rut, Instant momento) {
        return new SnapshotPersona(nombreLegible, rut, momento);
    }
}

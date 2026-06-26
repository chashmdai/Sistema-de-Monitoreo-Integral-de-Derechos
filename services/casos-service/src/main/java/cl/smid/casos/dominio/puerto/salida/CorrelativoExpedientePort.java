package cl.smid.casos.dominio.puerto.salida;

import cl.smid.casos.dominio.modelo.SerieExpediente;

/**
 * Puerto de salida para reservar correlativos de expediente de forma <strong>atómica y segura ante
 * concurrencia</strong>, por {@code (sede, año, serie)}.
 *
 * <p>El adaptador reutiliza el patrón ya probado en requerimientos: UPSERT de MySQL
 * ({@code INSERT ... ON DUPLICATE KEY UPDATE ... LAST_INSERT_ID(...)}) ejecutado sobre una única
 * conexión, que serializa las solicitudes concurrentes bajo bloqueo de fila y devuelve un número
 * exclusivo a cada solicitante.</p>
 */
public interface CorrelativoExpedientePort {

    /**
     * Reserva y devuelve el siguiente correlativo para la combinación indicada.
     *
     * @param idSedeAlt alt_key de la sede.
     * @param anio      año de la serie.
     * @param serie     OFICIAL o BETA (series aisladas entre sí).
     * @return correlativo exclusivo (≥ 1).
     */
    long reservarSiguiente(String idSedeAlt, int anio, SerieExpediente serie);
}

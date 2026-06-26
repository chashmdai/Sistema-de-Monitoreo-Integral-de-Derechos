package cl.smid.antecedentes.dominio.puerto.salida;

/**
 * Puerto de salida para la reserva atomica de correlativos de folio. El adaptador implementa
 * el modismo MySQL {@code INSERT ... ON DUPLICATE KEY UPDATE ultimo_valor =
 * LAST_INSERT_ID(ultimo_valor + 1)} sobre una unica conexion, dentro de la transaccion del
 * controlador, serializando las solicitudes concurrentes bajo bloqueo de fila. Nunca usa
 * {@code MAX()+1}.
 */
public interface CorrelativoFolioPort {

    /**
     * Reserva y devuelve el siguiente correlativo de una serie en un anio dado. Cada serie
     * (p. ej. {@code FA-2027}, {@code HZ-2027}) es independiente y reinicia anualmente.
     *
     * @param serie clave de serie (incluye el anio para aislar las secuencias por anio)
     * @param anio  anio de la serie (se persiste en la fila para trazabilidad)
     * @return correlativo exclusivo, monotonico dentro de la serie
     */
    long siguiente(String serie, int anio);
}

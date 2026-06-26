package cl.smid.casos.dominio.modelo;

/**
 * Datos de enriquecimiento on-demand del caso, resueltos contra requerimientos-service CON EL TOKEN
 * DEL USUARIO que consulta (propagación estándar, respetando territorial y G7).
 *
 * <p>La materialización inicial es un "esqueleto" (solo referencias del evento). El enriquecimiento
 * se DIFIERE: se calcula al abrir/consultar el caso. Para no vulnerar G7 ni el régimen de la Ficha
 * Reservada, este objeto transporta SOLO metadatos no sensibles (nunca nombres ni RUT de NNA). El
 * detalle sensible queda como costura para Vulneraciones (6.5), con sus propias reglas de acceso.</p>
 *
 * @param disponible       si el cruce pudo resolverse (el servicio de origen respondió y autorizó).
 * @param estadoRequerimiento estado actual del requerimiento de origen (metadato).
 * @param canal            canal de ingreso del requerimiento (metadato).
 * @param cantidadNnaAfectados cantidad de NNA afectados (conteo, NO identidades).
 */
public record DatosEnriquecimiento(boolean disponible, String estadoRequerimiento, String canal,
                                   Integer cantidadNnaAfectados) {

    /** Resultado vacío: el enriquecimiento no está activo o no pudo resolverse (tolerante). */
    public static DatosEnriquecimiento noDisponible() {
        return new DatosEnriquecimiento(false, null, null, null);
    }
}

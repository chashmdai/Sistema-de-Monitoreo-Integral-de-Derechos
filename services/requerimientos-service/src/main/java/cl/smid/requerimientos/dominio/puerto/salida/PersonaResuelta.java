package cl.smid.requerimientos.dominio.puerto.salida;

/**
 * Proyección mínima de una persona resuelta contra personas-service (6.2). Contiene solo lo
 * necesario para construir un {@code SnapshotPersona} de resiliencia: el alt_key (vínculo
 * persistente), el nombre legible y el RUT. No copia el registro completo.
 *
 * @param altKey        alt_key de la persona en personas-service
 * @param nombreLegible nombre para mostrar (puede ser nulo)
 * @param rut           RUT normalizado (puede ser nulo; un NNA puede no tenerlo)
 */
public record PersonaResuelta(String altKey, String nombreLegible, String rut) {
}

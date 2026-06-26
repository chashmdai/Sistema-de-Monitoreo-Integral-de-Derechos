package cl.smid.instituciones.dominio.modelo;

/**
 * Criterios de filtrado para el listado de instituciones. Todos los campos son
 * opcionales ({@code null} = sin restricción por ese criterio).
 *
 * <p>{@code tipoAlt} y {@code ambito} filtran por el tipo asociado: como la relación
 * con el tipo se almacena por PK interna escalar (override de diseño 6.10), el
 * adaptador los resuelve mediante una subconsulta sobre {@code tipo_institucion}.</p>
 *
 * @param tipoAlt      alt_key de un tipo concreto (puede ser nulo)
 * @param ambito       ámbito del tipo asociado (puede ser nulo)
 * @param texto        texto a buscar dentro del nombre (coincidencia parcial; puede ser nulo)
 * @param regionCodigo código de región exacto (puede ser nulo)
 * @param activa       filtro por estado activo ({@code true}/{@code false}); nulo = ambos
 * @param rut          RUT exacto ya validado (puede ser nulo); búsqueda por identificador
 */
public record FiltroInstituciones(
        String tipoAlt,
        Ambito ambito,
        String texto,
        String regionCodigo,
        Boolean activa,
        Rut rut) {
}

package cl.smid.instituciones.dominio.modelo;

/**
 * Criterios de filtrado para el listado de tipos de institución. Todos los campos
 * son opcionales ({@code null} = sin restricción por ese criterio).
 *
 * @param ambito  ámbito sectorial exacto a filtrar (puede ser nulo)
 * @param texto   texto a buscar dentro del nombre (coincidencia parcial; puede ser nulo)
 * @param vigente filtro por vigencia ({@code true}/{@code false}); nulo = ambos
 */
public record FiltroTipos(Ambito ambito, String texto, Boolean vigente) {
}

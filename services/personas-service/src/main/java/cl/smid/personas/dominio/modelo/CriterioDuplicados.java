package cl.smid.personas.dominio.modelo;

import java.time.LocalDate;

/**
 * Criterio de la prevalidación de duplicados (USR.01, Núcleo 5.5). Encapsula los datos
 * mínimos sobre los que el buscador coteja la base local: RUT (para coincidencia exacta) y
 * nombre+fecha (para coincidencias probables por similitud).
 *
 * <p>Todos los campos son opcionales; el buscador decide qué estrategias puede ejecutar según
 * lo que venga informado (p. ej. sin RUT no hay match exacto; sin nombre ni fecha no hay
 * candidatos difusos).</p>
 *
 * @param tipo            tipo de persona buscado (informativo; acota expectativas)
 * @param rut             RUT en formato de captura, o nulo
 * @param nombres         nombres de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param fechaNacimiento fecha de nacimiento
 */
public record CriterioDuplicados(
        TipoPersona tipo,
        String rut,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        LocalDate fechaNacimiento
) {
}

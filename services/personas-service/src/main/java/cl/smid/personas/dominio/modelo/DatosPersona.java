package cl.smid.personas.dominio.modelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Datos de entrada para crear o actualizar una persona, ya desacoplados del DTO de la API.
 * Todos los campos son opcionales a este nivel: la flexibilidad de ingreso (p. ej. un NNA
 * sólo con nombre, sin RUT) se modela permitiendo nulos; las reglas de negocio las aplica el
 * servicio de dominio.
 *
 * <p>En la actualización ({@code PUT}) la semántica es <i>partial-merge</i>: un campo nulo
 * significa "no tocar". Para {@code contactos} la convención es: lista nula = mantener los
 * existentes; lista no nula (incluida vacía) = reemplazar el conjunto completo.</p>
 *
 * @param tipo            tipo de persona (obligatorio en alta; en edición no se modifica)
 * @param rut             RUT en cualquier formato de captura, o nulo
 * @param nombres         nombres de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param razonSocial     razón social (persona jurídica)
 * @param fechaNacimiento fecha de nacimiento
 * @param sexo            sexo registrado
 * @param nacionalidad    nacionalidad
 * @param contactos       contactos a establecer (ver semántica de merge arriba)
 */
public record DatosPersona(
        TipoPersona tipo,
        String rut,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        String razonSocial,
        LocalDate fechaNacimiento,
        Sexo sexo,
        String nacionalidad,
        List<Contacto> contactos
) {
}

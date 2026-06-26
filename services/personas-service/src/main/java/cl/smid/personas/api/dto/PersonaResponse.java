package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Proyección pública completa de una persona ({@code GET /personas/{altKey}}, y respuesta de
 * {@code POST}/{@code PUT}). Encapsulación jerárquica (G7): se expone exclusivamente el
 * identificador opaco {@code altKey}; la PK interna ({@code id}) y los identificadores internos
 * jamás cruzan esta frontera.
 *
 * @param altKey          identificador público opaco (UUID)
 * @param tipo            tipo de persona
 * @param rut             RUT canónico {@code cuerpo-DV} o nulo
 * @param dv              dígito verificador o nulo
 * @param nombres         nombres de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param razonSocial     razón social
 * @param nombreLegible   nombre compuesto listo para mostrar
 * @param fechaNacimiento fecha de nacimiento
 * @param sexo            sexo registrado
 * @param nacionalidad    nacionalidad
 * @param idSede          alt_key de la sede a la que pertenece el registro
 * @param idUnidad        alt_key de la unidad a la que pertenece el registro
 * @param vigente         indicador de vigencia
 * @param creadoEn        instante de creación (UTC)
 * @param actualizadoEn   instante de última actualización (UTC)
 * @param contactos       lista de contactos
 */
@Schema(description = "Detalle publico completo de persona.")
public record PersonaResponse(
        @Schema(description = "Identificador publico opaco.", example = "a9b8c7d6-1111-2222-3333-444455556666")
        String altKey,
        @Schema(allowableValues = {"NNA", "ADULTO", "JURIDICA", "TESTIGO"}, example = "NNA")
        String tipo,
        @Schema(description = "RUT canonico cuerpo-DV, o null para NNA/personas sin RUT.", example = "12345678-5", nullable = true)
        String rut,
        @Schema(example = "5", nullable = true)
        String dv,
        @Schema(example = "Camila", nullable = true)
        String nombres,
        @Schema(example = "Reyes", nullable = true)
        String apellidoPaterno,
        @Schema(example = "Soto", nullable = true)
        String apellidoMaterno,
        @Schema(example = "Fundacion Ejemplo", nullable = true)
        String razonSocial,
        @Schema(example = "Camila Reyes")
        String nombreLegible,
        @Schema(example = "2015-03-10", nullable = true)
        LocalDate fechaNacimiento,
        @Schema(allowableValues = {"F", "M", "OTRO", "NO_INFORMA"}, example = "F", nullable = true)
        String sexo,
        @Schema(example = "Chilena", nullable = true)
        String nacionalidad,
        @Schema(description = "altKey de sede, no id interno.", example = "sede-a")
        String idSede,
        @Schema(description = "altKey de unidad, no id interno.", example = "unidad-1")
        String idUnidad,
        @Schema(example = "true")
        boolean vigente,
        @Schema(example = "2026-01-15T12:00:00Z")
        Instant creadoEn,
        @Schema(example = "2026-01-15T12:00:00Z")
        Instant actualizadoEn,
        @Schema(description = "Contactos publicos de la persona.")
        List<ContactoDTO> contactos
) {
}

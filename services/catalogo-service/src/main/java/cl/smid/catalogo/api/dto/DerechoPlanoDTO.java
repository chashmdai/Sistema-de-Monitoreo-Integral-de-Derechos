package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Proyección plana de un derecho (formato {@code ?formato=plano}). En lugar de anidar, expone
 * la relación con el padre mediante su <b>alt_key</b> ({@code idPadreAltKey}); nunca la FK
 * interna. Es nulo en las raíces.
 *
 * @param altKey        identificador público opaco del derecho
 * @param codigo        código estable
 * @param nombre        nombre legible
 * @param nivel         profundidad en el árbol
 * @param idPadreAltKey alt_key del padre, o {@code null} si es una raíz
 */
@Schema(description = "Derecho en formato plano, con referencia al padre por altKey.")
public record DerechoPlanoDTO(
        @Schema(description = "Identificador publico opaco.", example = "11111111-1111-4111-8111-000000000012")
        String altKey,
        @Schema(example = "EDU.ACCESO")
        String codigo,
        @Schema(example = "Acceso y permanencia")
        String nombre,
        @Schema(example = "1")
        short nivel,
        @Schema(description = "altKey del padre; null si raiz.", example = "11111111-1111-4111-8111-000000000003", nullable = true)
        String idPadreAltKey
) {}

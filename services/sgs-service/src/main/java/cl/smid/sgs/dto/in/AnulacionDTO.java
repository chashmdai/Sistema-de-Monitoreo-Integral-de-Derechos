package cl.smid.sgs.dto.in;

import jakarta.validation.constraints.NotBlank;

/** Borrado lógico: motivo obligatorio (ENT-10). El autor sale del JWT. */
public record AnulacionDTO(@NotBlank String motivo) {}

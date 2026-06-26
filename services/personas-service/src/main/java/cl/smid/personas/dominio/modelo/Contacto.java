package cl.smid.personas.dominio.modelo;

import java.util.Objects;

/**
 * Dato de contacto de una persona (teléfono, email o dirección). Objeto de valor del
 * agregado {@link Persona}; inmutable.
 *
 * @param tipo  categoría del contacto (no nulo)
 * @param valor valor del contacto, ya saneado/normalizado (no en blanco)
 */
public record Contacto(TipoContacto tipo, String valor) {

    public Contacto {
        Objects.requireNonNull(tipo, "El tipo de contacto es obligatorio");
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El valor del contacto no puede estar vacío");
        }
        valor = valor.trim();
    }
}

package cl.smid.personas.dominio.modelo;

import java.util.Objects;

/**
 * Contexto territorial del solicitante, derivado del token validado (§2.3) y entregado por
 * el controlador al dominio. Aísla al núcleo de Spring Security: el servicio razona sobre
 * este record, no sobre el {@code Authentication} de la infraestructura.
 *
 * @param actor   identidad del usuario (claim {@code sub}, un alt_key UUID); queda como
 *                {@code creado_por} al estampar registros
 * @param alcance radio de visibilidad territorial (UNIDAD | SEDE | NACIONAL)
 * @param idSede  alt_key de la sede del usuario (claim {@code idSede}); puede ser nulo en
 *                alcance NACIONAL
 * @param idUnidad alt_key de la unidad del usuario (claim {@code idUnidad}); relevante en
 *                 alcance UNIDAD
 */
public record ContextoTerritorial(String actor, Alcance alcance, String idSede, String idUnidad) {

    public ContextoTerritorial {
        Objects.requireNonNull(actor, "El actor (sub) es obligatorio en el contexto");
        Objects.requireNonNull(alcance, "El alcance es obligatorio en el contexto");
    }
}

package cl.smid.catalogo.infraestructura.seguridad;

import java.util.List;

/**
 * Contexto de sesión corporativa: los atributos del usuario autenticado extraídos del JWT
 * validado. Es el {@code principal} que la capa de seguridad coloca en el contexto de
 * Spring Security y que la capa api lee para construir los eventos de dominio.
 *
 * <p>Incluye los atributos de segmentación geográfica del ecosistema ({@code idSede},
 * {@code idUnidad}, {@code alcance}). El catálogo es de alcance nacional (datos de
 * referencia compartidos), de modo que estos atributos no restringen las lecturas, pero se
 * leen y propagan a los eventos para mantener la trazabilidad y la coherencia del ecosistema.</p>
 *
 * @param subAltKey alt_key del usuario (claim {@code sub})
 * @param roles     roles del usuario (claim {@code roles}); nunca nulo
 * @param idSede    alt_key de la sede (claim {@code idSede}; puede ser nulo)
 * @param idUnidad  alt_key de la unidad (claim {@code idUnidad}; puede ser nulo)
 * @param alcance   alcance territorial declarado: {@code UNIDAD}, {@code SEDE} o {@code NACIONAL}
 * @param nombre    nombre legible del usuario (claim {@code nombre}; informativo)
 */
public record ContextoSesion(
        String subAltKey,
        List<String> roles,
        String idSede,
        String idUnidad,
        String alcance,
        String nombre
) {
    public ContextoSesion {
        roles = (roles == null) ? List.of() : List.copyOf(roles);
    }
}

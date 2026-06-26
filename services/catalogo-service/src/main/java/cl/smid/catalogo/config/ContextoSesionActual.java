package cl.smid.catalogo.config;

import cl.smid.catalogo.dominio.puerto.entrada.ActorEvento;
import cl.smid.catalogo.infraestructura.seguridad.ContextoSesion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Acceso conveniente al {@link ContextoSesion} del usuario autenticado, leyéndolo del
 * {@code SecurityContextHolder}. Aísla a la capa api del detalle de Spring Security: el
 * controlador solo conoce este ayudante y el {@link ActorEvento} del dominio.
 */
@Component
public class ContextoSesionActual {

    /** Contexto de sesión actual, si hay un usuario autenticado con principal de sesión. */
    public Optional<ContextoSesion> actual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion != null && autenticacion.getPrincipal() instanceof ContextoSesion contexto) {
            return Optional.of(contexto);
        }
        return Optional.empty();
    }

    /**
     * Construye el {@link ActorEvento} para enriquecer los eventos de dominio a partir de la
     * sesión actual. Si no hubiese sesión (no debería ocurrir en endpoints autenticados), se
     * usa el actor de sistema como salvaguarda.
     */
    public ActorEvento comoActor() {
        return actual()
                .map(c -> new ActorEvento(c.subAltKey(), c.idSede(), c.idUnidad()))
                .orElseGet(ActorEvento::sistema);
    }
}

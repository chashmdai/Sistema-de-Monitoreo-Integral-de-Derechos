package cl.smid.personas.infraestructura.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que revalida el JWT en cada petición (defensa en profundidad, DT-3, Núcleo 2.4) y, si
 * es válido, establece el {@code Authentication} en el contexto de seguridad con el
 * {@link UsuarioAutenticado} como principal.
 *
 * <p>Comportamiento ante token ausente o inválido: el filtro <b>no</b> escribe la respuesta de
 * error ni interrumpe la cadena; simplemente no autentica y continúa. Será el
 * {@link PuntoEntradaNoAutorizado} quien emita el 401 cuando la regla de autorización rechace la
 * petición no autenticada. Esto mantiene una única fuente de verdad para el sobre 401.</p>
 *
 * <p>Las autoridades se derivan de los roles del token con el prefijo {@code ROLE_} que exige
 * Spring Security, de modo que una eventual autorización por rol funcione sin cambios.</p>
 */
@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FiltroAutenticacionJwt.class);

    private static final String ENCABEZADO = "Authorization";
    private static final String PREFIJO_BEARER = "Bearer ";

    private final ValidadorTokenJwt validador;

    public FiltroAutenticacionJwt(ValidadorTokenJwt validador) {
        this.validador = validador;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = extraerToken(request);

        // Sólo se intenta autenticar si hay token y aún no hay autenticación en el contexto.
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UsuarioAutenticado usuario = validador.validar(token);

                List<SimpleGrantedAuthority> autoridades = usuario.roles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                        .toList();

                var autenticacion = new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            } catch (Exception ex) {
                // Token presente pero inválido: se limpia el contexto y se deja seguir la cadena;
                // el punto de entrada producirá el 401. No se filtra el detalle del fallo al cliente.
                SecurityContextHolder.clearContext();
                log.debug("Token JWT inválido: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /** Extrae el token del encabezado {@code Authorization: Bearer <token>}; nulo si no aplica. */
    private static String extraerToken(HttpServletRequest request) {
        String cabecera = request.getHeader(ENCABEZADO);
        if (cabecera != null && cabecera.startsWith(PREFIJO_BEARER)) {
            String valor = cabecera.substring(PREFIJO_BEARER.length()).trim();
            return valor.isEmpty() ? null : valor;
        }
        return null;
    }
}

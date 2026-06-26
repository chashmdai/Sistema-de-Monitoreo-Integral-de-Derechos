package cl.smid.catalogo.config;

import cl.smid.catalogo.dominio.excepcion.CatalogoException;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import cl.smid.catalogo.infraestructura.seguridad.ContextoSesion;
import cl.smid.catalogo.infraestructura.seguridad.ValidadorTokenJwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que autentica cada solicitud a partir del JWT del encabezado {@code Authorization}.
 *
 * <h2>Comportamiento</h2>
 * <ul>
 *   <li>Si no hay encabezado {@code Bearer}, deja pasar la solicitud <b>sin autenticar</b>: será
 *       el punto de entrada de seguridad quien rechace las rutas protegidas con 401.</li>
 *   <li>Si hay token, lo valida con {@link ValidadorTokenJwt}; en caso de éxito coloca en el
 *       contexto de seguridad un {@code Authentication} cuyo principal es el
 *       {@link ContextoSesion} y cuyas autoridades son los roles con prefijo {@code ROLE_}.</li>
 *   <li>Si el token es inválido o expiró, responde de inmediato con el sobre 401
 *       ({@code AUTZ-003}) y corta la cadena.</li>
 * </ul>
 *
 * <p>No se anota como {@code @Component} a propósito: se registra explícitamente en la cadena
 * de filtros de seguridad, evitando que el contenedor de servlets lo registre por duplicado.</p>
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";
    private static final String PREFIJO_ROL = "ROLE_";

    private final ValidadorTokenJwt validador;
    private final EscritorErrorSeguridad escritorError;

    public JwtAuthFilter(ValidadorTokenJwt validador, EscritorErrorSeguridad escritorError) {
        this.validador = validador;
        this.escritorError = escritorError;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest solicitud,
                                    @NonNull HttpServletResponse respuesta,
                                    @NonNull FilterChain cadena) throws ServletException, IOException {
        String encabezado = solicitud.getHeader(HttpHeaders.AUTHORIZATION);
        if (encabezado == null || !encabezado.startsWith(PREFIJO_BEARER)) {
            cadena.doFilter(solicitud, respuesta);
            return;
        }

        String token = encabezado.substring(PREFIJO_BEARER.length()).trim();
        try {
            ContextoSesion contexto = validador.validar(token);

            List<SimpleGrantedAuthority> autoridades = contexto.roles().stream()
                    .map(rol -> new SimpleGrantedAuthority(PREFIJO_ROL + rol))
                    .toList();

            var autenticacion = new UsernamePasswordAuthenticationToken(contexto, null, autoridades);
            autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));
            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        } catch (CatalogoException e) {
            // Token presente pero inválido/expirado: no se delega al punto de entrada, se responde aquí.
            SecurityContextHolder.clearContext();
            escritorError.escribir(solicitud, respuesta, CodigoError.NO_AUTENTICADO, e.getMessage());
            return;
        }

        cadena.doFilter(solicitud, respuesta);
    }
}

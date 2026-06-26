package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que autentica cada petición a partir del JWT del encabezado
 * {@code Authorization: Bearer ...}. Si el token es válido, deja el
 * {@link ContextoSesion} como atributo de la petición (para
 * {@link ProveedorContextoRequest}) y establece la autenticación en el contexto de
 * seguridad. Si falta o es inválido, no autentica y deja que el punto de entrada
 * responda {@code 401}.
 */
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    /** Nombre del atributo de petición donde se guarda el contexto de sesión. */
    public static final String ATRIBUTO_CONTEXTO = "smid.contextoSesion";

    private static final String ENCABEZADO = "Authorization";
    private static final String PREFIJO = "Bearer ";

    private final ValidadorJwt validador;

    public FiltroAutenticacionJwt(ValidadorJwt validador) {
        this.validador = validador;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String encabezado = request.getHeader(ENCABEZADO);
        if (encabezado != null && encabezado.startsWith(PREFIJO)) {
            String token = encabezado.substring(PREFIJO.length()).trim();
            try {
                ContextoSesion contexto = validador.validar(token);
                request.setAttribute(ATRIBUTO_CONTEXTO, contexto);

                List<SimpleGrantedAuthority> autoridades = contexto.roles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(contexto, null, autoridades);
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            } catch (Exception ex) {
                // Token presente pero inválido: no se autentica; el punto de entrada
                // devolverá 401. Se limpia cualquier contexto residual.
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}

package cl.smid.antecedentes.infraestructura.seguridad;

import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de autenticacion: extrae el {@code Bearer}, valida el JWT y, si es valido, publica el
 * {@link ContextoSesion} en los request attributes y fija la autenticacion en el
 * {@code SecurityContext}. Si no hay token o es invalido, no autentica: la autorizacion de
 * Spring Security responde 401 ({@code AUTZ-003}) via el punto de entrada.
 *
 * <p>Las comprobaciones de rol (revision/admin) y territoriales las realiza el dominio sobre el
 * {@link ContextoSesion}; las autoridades de Spring solo distinguen autenticado de anonimo.</p>
 */
@Component
public class FiltroAutenticacion extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FiltroAutenticacion.class);
    private static final String PREFIJO_BEARER = "Bearer ";

    private final ValidadorToken validadorToken;

    public FiltroAutenticacion(ValidadorToken validadorToken) {
        this.validadorToken = validadorToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String cabecera = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (cabecera != null && cabecera.startsWith(PREFIJO_BEARER)) {
            String token = cabecera.substring(PREFIJO_BEARER.length()).trim();
            try {
                ContextoSesion contexto = validadorToken.validar(token);
                publicarContexto(request, contexto);
                fijarAutenticacion(request, contexto);
            } catch (TokenInvalidoException e) {
                log.debug("Token rechazado: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

    private void publicarContexto(HttpServletRequest request, ContextoSesion contexto) {
        request.setAttribute(ProveedorContexto.ATRIBUTO_CONTEXTO, contexto);
        RequestAttributes atributos = RequestContextHolder.getRequestAttributes();
        if (atributos != null) {
            atributos.setAttribute(ProveedorContexto.ATRIBUTO_CONTEXTO, contexto, RequestAttributes.SCOPE_REQUEST);
        }
    }

    private void fijarAutenticacion(HttpServletRequest request, ContextoSesion contexto) {
        List<SimpleGrantedAuthority> autoridades = contexto.roles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(contexto.usuarioAlt(), null, autoridades);
        auth.setDetails(contexto);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

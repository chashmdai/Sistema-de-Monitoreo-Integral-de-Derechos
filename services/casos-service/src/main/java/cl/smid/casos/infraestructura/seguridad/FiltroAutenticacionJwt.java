package cl.smid.casos.infraestructura.seguridad;

import cl.smid.casos.dominio.excepcion.CodigoError;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Filtro que valida el JWT entrante y, si es válido, establece el {@link ContextoTerritorial} como
 * principal autenticado. Si hay un token presente pero inválido, corta la cadena y responde 401
 * (AUTZ-003); si no hay token, deja continuar (el punto de entrada de seguridad responderá 401 para
 * los endpoints protegidos).
 */
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final ValidadorJwt validadorJwt;
    private final ObjectMapper objectMapper;

    public FiltroAutenticacionJwt(ValidadorJwt validadorJwt, ObjectMapper objectMapper) {
        this.validadorJwt = validadorJwt;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String cabecera = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (cabecera == null || !cabecera.startsWith(PREFIJO_BEARER)) {
            filterChain.doFilter(request, response); // sin token: lo resuelve el punto de entrada
            return;
        }

        String token = cabecera.substring(PREFIJO_BEARER.length()).trim();
        try {
            ContextoTerritorial ctx = validadorJwt.validar(token);
            List<GrantedAuthority> autoridades = ctx.roles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(ctx, null, autoridades);
            SecurityContextHolder.getContext().setAuthentication(autenticacion);
            filterChain.doFilter(request, response);
        } catch (TokenInvalidoException ex) {
            SecurityContextHolder.clearContext();
            escribir401(request, response, ex.getMessage());
        }
    }

    private void escribir401(HttpServletRequest request, HttpServletResponse response, String detalle)
            throws IOException {
        CodigoError codigo = CodigoError.AUTZ_003;
        response.setStatus(codigo.httpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> sobre = new LinkedHashMap<>();
        sobre.put("status", codigo.httpStatus());
        sobre.put("error", codigo.error());
        sobre.put("codigo", codigo.codigo());
        sobre.put("mensaje", detalle == null ? "Token inválido." : detalle);
        sobre.put("ruta", request.getRequestURI());
        sobre.put("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getOutputStream(), sobre);
    }
}

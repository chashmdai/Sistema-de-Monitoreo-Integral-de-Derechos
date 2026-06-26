package cl.smid.requerimientos.infraestructura.seguridad;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que autentica cada petición a partir del JWT (cabecera {@code Authorization: Bearer ...}).
 *
 * <p>Si el token es válido, construye la {@code Authentication} con el principal
 * {@link UsuarioAutenticado} y las autoridades {@code ROLE_<codigo>}, y deposita el token crudo en
 * el {@link PortadorToken} para su propagación a los servicios aguas abajo. Si el token está
 * presente pero es inválido, delega en el {@link EntryPointNoAutenticado} (401). Si no hay token,
 * deja seguir la cadena: la autorización denegará las rutas protegidas con 401.</p>
 */
public class FiltroJwt extends OncePerRequestFilter {

    private static final String CABECERA = "Authorization";
    private static final String PREFIJO = "Bearer ";

    private final ProveedorValidacionJwt proveedor;
    private final EntryPointNoAutenticado entryPoint;
    private final PortadorToken portadorToken;

    public FiltroJwt(ProveedorValidacionJwt proveedor, EntryPointNoAutenticado entryPoint,
                     PortadorToken portadorToken) {
        this.proveedor = proveedor;
        this.entryPoint = entryPoint;
        this.portadorToken = portadorToken;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String cabecera = request.getHeader(CABECERA);

        if (cabecera == null || !cabecera.startsWith(PREFIJO)) {
            // Sin token: la decisión de autorización (rutas protegidas) la toma Spring Security.
            filterChain.doFilter(request, response);
            return;
        }

        String token = cabecera.substring(PREFIJO.length()).trim();
        try {
            UsuarioAutenticado usuario = proveedor.validar(token);
            autenticar(request, usuario);
            portadorToken.establecer(token);
        } catch (JwtException | IllegalArgumentException e) {
            // Token presente pero inválido: limpiar contexto y responder 401 (AUTZ-003).
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new BadCredentialsException("Token inválido", e));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(HttpServletRequest request, UsuarioAutenticado usuario) {
        List<SimpleGrantedAuthority> autoridades = usuario.roles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()))
                .toList();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

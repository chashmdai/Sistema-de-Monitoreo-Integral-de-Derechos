package cl.smid.requerimientos.config;

import cl.smid.requerimientos.infraestructura.seguridad.EntryPointNoAutenticado;
import cl.smid.requerimientos.infraestructura.seguridad.FiltroJwt;
import cl.smid.requerimientos.infraestructura.seguridad.ManejadorAccesoDenegado;
import cl.smid.requerimientos.infraestructura.seguridad.PortadorToken;
import cl.smid.requerimientos.infraestructura.seguridad.ProveedorValidacionJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad HTTP. Define una cadena sin estado (el JWT porta toda la identidad),
 * registra el {@link FiltroJwt} antes del filtro de usuario/contraseña y enchufa los manejadores de
 * 401 ({@code AUTZ-003}) y 403 ({@code AUTZ-004}). Habilita la seguridad por método para el
 * {@code @PreAuthorize} de Coordinación en la admisibilidad.
 *
 * <p>Rutas públicas: únicamente las sondas de salud/info de Actuator. Todo lo demás exige
 * autenticación; la autorización fina (territorial y de rol) ocurre en el dominio y en el
 * controlador.</p>
 */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

    private final ProveedorValidacionJwt proveedorJwt;
    private final EntryPointNoAutenticado entryPoint;
    private final ManejadorAccesoDenegado accesoDenegado;
    private final PortadorToken portadorToken;

    public SeguridadConfig(ProveedorValidacionJwt proveedorJwt, EntryPointNoAutenticado entryPoint,
                           ManejadorAccesoDenegado accesoDenegado, PortadorToken portadorToken) {
        this.proveedorJwt = proveedorJwt;
        this.entryPoint = entryPoint;
        this.accesoDenegado = accesoDenegado;
        this.portadorToken = portadorToken;
    }

    /**
     * Define la cadena de filtros de seguridad.
     *
     * @param http builder de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si la configuración falla
     */
    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {
        FiltroJwt filtroJwt = new FiltroJwt(proveedorJwt, entryPoint, portadorToken);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/health/**",
                                "/actuator/info").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accesoDenegado))
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

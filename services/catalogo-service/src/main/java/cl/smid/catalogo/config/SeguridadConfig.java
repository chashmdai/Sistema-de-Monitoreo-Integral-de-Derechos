package cl.smid.catalogo.config;

import cl.smid.catalogo.infraestructura.seguridad.ValidadorTokenJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad del servicio (Spring Security 6).
 *
 * <h2>Modelo de acceso</h2>
 * <ul>
 *   <li><b>Sin estado</b> ({@code STATELESS}): no hay sesión de servidor; la identidad viaja en
 *       el JWT en cada petición. CSRF se desactiva por ser una API de tokens sin cookies.</li>
 *   <li>{@code /actuator/health} e {@code /actuator/info} son públicos (sondas de readiness/liveness).</li>
 *   <li>Las <b>lecturas</b> ({@code GET /catalogo/**}) requieren solo autenticación.</li>
 *   <li>Las <b>escrituras</b> ({@code POST/PUT/DELETE /catalogo/**}) requieren rol de
 *       Administración (cualquiera de los configurados en {@code smid.seguridad}).</li>
 * </ul>
 *
 * <h2>Coherencia de autoridades</h2>
 * <p>El {@link JwtAuthFilter} otorga autoridades con prefijo {@code ROLE_} a partir de los roles
 * del token; por eso aquí la autorización de escritura se expresa con esas mismas autoridades
 * ({@code ROLE_<rol>}), garantizando que filtro y reglas hablen el mismo idioma.</p>
 *
 * <p>Comportamiento esperado de errores: una escritura <b>sin token</b> deriva en 401
 * ({@code AUTZ-003}, vía punto de entrada); <b>con token pero sin rol</b>, en 403
 * ({@code AUTZ-004}, vía manejador de acceso denegado).</p>
 */
@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    private final PropiedadesSeguridad propiedadesSeguridad;

    public SeguridadConfig(PropiedadesSeguridad propiedadesSeguridad) {
        this.propiedadesSeguridad = propiedadesSeguridad;
    }

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(
            HttpSecurity http,
            ValidadorTokenJwt validador,
            EscritorErrorSeguridad escritorError,
            PuntoEntradaJwt puntoEntrada,
            ManejadorAccesoDenegado manejadorAccesoDenegado) throws Exception {

        // Roles de Administración expresados como autoridades ROLE_*, en coherencia con el filtro.
        String[] autoridadesAdministracion = propiedadesSeguridad.getRolesAdministracion().stream()
                .map(rol -> "ROLE_" + rol)
                .toArray(String[]::new);

        JwtAuthFilter filtroJwt = new JwtAuthFilter(validador, escritorError);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autz -> autz
                        .requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/catalogo/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/catalogo/**").hasAnyAuthority(autoridadesAdministracion)
                        .requestMatchers(HttpMethod.PUT, "/catalogo/**").hasAnyAuthority(autoridadesAdministracion)
                        .requestMatchers(HttpMethod.DELETE, "/catalogo/**").hasAnyAuthority(autoridadesAdministracion)
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(puntoEntrada)
                        .accessDeniedHandler(manejadorAccesoDenegado))
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

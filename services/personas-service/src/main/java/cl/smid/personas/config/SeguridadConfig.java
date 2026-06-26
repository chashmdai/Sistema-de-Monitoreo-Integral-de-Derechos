package cl.smid.personas.config;

import cl.smid.personas.infraestructura.seguridad.FiltroAutenticacionJwt;
import cl.smid.personas.infraestructura.seguridad.ManejadorAccesoDenegado;
import cl.smid.personas.infraestructura.seguridad.PuntoEntradaNoAutorizado;
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
 * Configuración de seguridad HTTP del servicio (Núcleo 2.4). Cada microservicio revalida el
 * token aunque el Gateway ya autentique (defensa en profundidad, DT-3).
 *
 * <p>Decisiones:</p>
 * <ul>
 *   <li><b>Sin estado</b> ({@link SessionCreationPolicy#STATELESS}): no hay sesión de servidor;
 *       la identidad viaja en el JWT en cada petición.</li>
 *   <li><b>CSRF deshabilitado</b>: no aplica a una API sin cookies de sesión, consumida por el
 *       Gateway y otros servicios.</li>
 *   <li><b>Login básico, formulario y logout deshabilitados</b>: el servicio no autentica usuarios
 *       localmente ni mantiene sesión; sólo acepta JWT revalidado por el filtro propio. Esto evita
 *       que Spring Security active mecanismos por defecto como usuario/contraseña generado en
 *       memoria.</li>
 *   <li>Rutas públicas: sólo los endpoints de observabilidad {@code /actuator/health} e
 *       {@code /actuator/info}. Todo lo demás requiere autenticación.</li>
 *   <li>El {@link FiltroAutenticacionJwt} se registra antes del filtro de usuario/contraseña
 *       para poblar el contexto a partir del token.</li>
 *   <li>{@link EnableMethodSecurity} habilita una eventual autorización por rol a nivel de método
 *       sin reescribir esta clase.</li>
 * </ul>
 *
 * <p>Nota: las rutas se expresan sin el prefijo {@code /api}, que el Gateway elimina
 * ({@code StripPrefix}) antes de enrutar al servicio; internamente los controladores cuelgan de
 * {@code /personas/...}.</p>
 */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;
    private final PuntoEntradaNoAutorizado puntoEntradaNoAutorizado;
    private final ManejadorAccesoDenegado manejadorAccesoDenegado;

    public SeguridadConfig(FiltroAutenticacionJwt filtroAutenticacionJwt,
                           PuntoEntradaNoAutorizado puntoEntradaNoAutorizado,
                           ManejadorAccesoDenegado manejadorAccesoDenegado) {
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
        this.puntoEntradaNoAutorizado = puntoEntradaNoAutorizado;
        this.manejadorAccesoDenegado = manejadorAccesoDenegado;
    }

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // El servicio no usa autenticación HTTP Basic, formulario ni logout.
                // La única autenticación aceptada es el Bearer JWT validado por FiltroAutenticacionJwt.
                // Esto mantiene el servicio stateless y evita caminos de autenticación locales
                // innecesarios para un microservicio detrás del Gateway.
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Observabilidad pública para el orquestador (liveness/readiness e info).
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // El resto exige un token válido.
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(puntoEntradaNoAutorizado)
                        .accessDeniedHandler(manejadorAccesoDenegado))
                // El filtro JWT puebla el contexto antes del filtro estándar de usuario/contraseña.
                .addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

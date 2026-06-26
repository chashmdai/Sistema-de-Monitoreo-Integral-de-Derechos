package cl.smid.antecedentes.config;

import cl.smid.antecedentes.infraestructura.seguridad.FiltroAutenticacion;
import cl.smid.antecedentes.infraestructura.seguridad.ManejadorAccesoDenegado;
import cl.smid.antecedentes.infraestructura.seguridad.PuntoEntradaNoAutenticado;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion de seguridad: API stateless, sin CSRF (no hay sesion ni formularios). Solo
 * {@code /actuator/health} e {@code /actuator/info} son publicos; el resto exige autenticacion.
 * El {@link FiltroAutenticacion} valida el JWT antes del filtro de usuario/clave. Las respuestas
 * 401/403 usan el sobre de error unificado.
 *
 * <p>Las comprobaciones de rol y territoriales viven en el dominio; aqui solo se exige estar
 * autenticado para acceder a cualquier endpoint de negocio.</p>
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final FiltroAutenticacion filtroAutenticacion;
    private final PuntoEntradaNoAutenticado puntoEntradaNoAutenticado;
    private final ManejadorAccesoDenegado manejadorAccesoDenegado;

    public ConfiguracionSeguridad(FiltroAutenticacion filtroAutenticacion,
                                  PuntoEntradaNoAutenticado puntoEntradaNoAutenticado,
                                  ManejadorAccesoDenegado manejadorAccesoDenegado) {
        this.filtroAutenticacion = filtroAutenticacion;
        this.puntoEntradaNoAutenticado = puntoEntradaNoAutenticado;
        this.manejadorAccesoDenegado = manejadorAccesoDenegado;
    }

    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer -> AbstractHttpConfigurer.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/health/**",
                                "/actuator/info").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(puntoEntradaNoAutenticado)
                        .accessDeniedHandler(manejadorAccesoDenegado))
                .addFilterBefore(filtroAutenticacion, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

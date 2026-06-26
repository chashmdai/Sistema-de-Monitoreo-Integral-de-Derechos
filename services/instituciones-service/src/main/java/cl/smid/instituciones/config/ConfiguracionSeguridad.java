package cl.smid.instituciones.config;

import cl.smid.instituciones.infraestructura.seguridad.FiltroAutenticacionJwt;
import cl.smid.instituciones.infraestructura.seguridad.ManejadorAccesoDenegado;
import cl.smid.instituciones.infraestructura.seguridad.PuntoEntradaNoAutenticado;
import cl.smid.instituciones.infraestructura.seguridad.ValidadorJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad: API REST sin estado, autenticación por JWT y sobre de
 * error unificado para los rechazos {@code 401}/{@code 403}.
 *
 * <p>Solo {@code /actuator/health} es público; el resto exige autenticación. La
 * autorización de escritura por rol administrador la decide el dominio (override #6),
 * por lo que aquí no se declaran reglas por rol salvo la exigencia de estar autenticado.</p>
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Bean
    SecurityFilterChain cadenaSeguridad(HttpSecurity http,
                                        ValidadorJwt validadorJwt,
                                        PuntoEntradaNoAutenticado puntoEntrada,
                                        ManejadorAccesoDenegado manejadorAccesoDenegado) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(gestion -> gestion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(manejo -> manejo
                        .authenticationEntryPoint(puntoEntrada)
                        .accessDeniedHandler(manejadorAccesoDenegado))
                .addFilterBefore(new FiltroAutenticacionJwt(validadorJwt),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

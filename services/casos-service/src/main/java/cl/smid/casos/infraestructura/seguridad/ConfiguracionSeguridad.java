package cl.smid.casos.infraestructura.seguridad;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad del servicio. API sin estado (sin sesión), sin CSRF (no hay cookies), y
 * con el filtro JWT propio antepuesto. Todos los endpoints {@code /casos/**} exigen autenticación;
 * la autorización fina (rol de Coordinación, alcance territorial) se resuelve en el dominio.
 */
@Configuration
@EnableConfigurationProperties(PropiedadesJwt.class)
public class ConfiguracionSeguridad {

    @Bean
    public ValidadorJwt validadorJwt(PropiedadesJwt propiedades) {
        return new ValidadorJwt(propiedades);
    }

    @Bean
    public FiltroAutenticacionJwt filtroAutenticacionJwt(ValidadorJwt validadorJwt,
                                                         ObjectMapper objectMapper) {
        return new FiltroAutenticacionJwt(validadorJwt, objectMapper);
    }

    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http,
                                               FiltroAutenticacionJwt filtroJwt,
                                               PuntoEntradaNoAutenticado puntoEntrada) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/casos/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(puntoEntrada))
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

package cl.smid.sgs.config;

import cl.smid.sgs.exception.SgsAccessDeniedHandler;
import cl.smid.sgs.exception.SgsAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Opción B: converter de roles PUESTO, matchers por rol APAGADOS.
 * - Autenticación obligatoria (decisión #19): el 'sub' del JWT es la trazabilidad legal sobre PII de NNA.
 * - aud/iss NO se validan (SEC-3 / lección del loop 401 de ESNNA): el Auth emite {sub, roles, idSede, idUnidad, iat, exp}.
 * - Roles mapeados desde el claim 'roles' (ya vienen con prefijo ROLE_), listos para cerrar endpoints sin re-cablear.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final SgsProperties props;
    private final SgsAuthenticationEntryPoint entryPoint;
    private final SgsAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(SgsProperties props,
                          SgsAuthenticationEntryPoint entryPoint,
                          SgsAccessDeniedHandler accessDeniedHandler) {
        this.props = props;
        this.entryPoint = entryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // health/info abiertos para el orquestador del cluster (SEC-5)
                .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                // Opción B: por ahora cualquier autenticado opera todo. El converter ya dejó los roles listos.
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );
        return http.build();
    }

    /** Firma simétrica HS256; sin validadores de aud/iss (SEC-3). */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /** Mapea el claim 'roles' (prefijo vacío: ya vienen como ROLE_*) y fija 'sub' como principal. */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName(props.getRolesClaim());
        authorities.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        converter.setPrincipalClaimName("sub");
        return converter;
    }
}

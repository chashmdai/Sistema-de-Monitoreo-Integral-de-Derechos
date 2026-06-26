package cl.smid.esnna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Seguridad del resource server.
 *  - v1 (intranet): cualquier usuario AUTENTICADO puede operar ESNNA. Se mantiene
 *    la autenticación (firma + expiración del JWT del Auth Service) para conservar
 *    la trazabilidad/auditoría por usuario (sub del token).
 *  - SEC-3: validación opcional de issuer/audience (desactivadas si van vacías).
 *  - SEC-5: /actuator/health permitido sin auth (healthcheck de contenedor).
 *  - ERR-1: 401/403 se serializan como ApiError vía entry point / handler inyectados.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${esnna.security.issuer:}")
    private String issuer;

    @Value("${esnna.security.audience:}")
    private String audience;

    @Value("${esnna.security.claim-roles:roles}")
    private String claimRoles;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)   // CORS global en el api-gateway (8080)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Healthcheck del contenedor sin token.
                .requestMatchers("/actuator/health/**").permitAll()
                // Documentación (si springdoc está activo) sin token.
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // v1 (intranet): cualquier usuario AUTENTICADO puede operar ESNNA.
                // Para reactivar autorización por rol, restaurar requestMatchers con hasAuthority.
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(new JwtTimestampValidator()); // exp/nbf
        if (issuer != null && !issuer.isBlank()) {
            validators.add(new JwtIssuerValidator(issuer));
        }
        if (audience != null && !audience.isBlank()) {
            validators.add(new JwtClaimValidator<List<String>>(
                    "aud", aud -> aud != null && aud.contains(audience)));
        }
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));
        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthorityPrefix("");
        authorities.setAuthoritiesClaimName(claimRoles);

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}
package cl.smid.esnna.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Documentación OpenAPI del motor ESNNA. Declara el esquema bearer JWT para que
 * el "Authorize" de Swagger UI funcione con los tokens del Auth Service.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearer-jwt";

    @Bean
    public OpenAPI esnnaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ESNNA Motor Service")
                        .version("v2")
                        .description("Análisis y priorización de casos ESNNA (protocolo PR-PDR-05)."))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

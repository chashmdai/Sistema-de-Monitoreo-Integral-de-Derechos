package cl.smid.smidservice.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuracion web del microservicio.
 *
 * El BFF Gateway (puerto 8080) maneja CORS, autenticacion JWT y rate limiting
 * antes de rutear hacia este servicio. Por lo tanto, este microservicio NO
 * configura CORS local y queda accesible solo desde la red interna del cluster.
 *
 * Si se requiere acceso directo (testing local con Postman), se puede activar
 * CORS aqui con un WebMvcConfigurer.
 */
@Configuration
public class WebConfig {
}

package cl.smid.personas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Punto de arranque del microservicio de Personas (6.2) del Núcleo Fundacional SMID.
 *
 * <p>{@link ConfigurationPropertiesScan} habilita el enlace de las propiedades tipadas
 * ({@code PropiedadesJwt}, bajo {@code smid.jwt}) sin necesidad de registrarlas una a una.</p>
 *
 * <p>El servicio arranca con la base de datos vacía: Flyway crea únicamente el esquema
 * ({@code V1__inicial.sql}); no hay semilla ni migración del padrón histórico (ver README,
 * sección "Decisión de arranque sin migración").</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class PersonasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonasApplication.class, args);
    }
}

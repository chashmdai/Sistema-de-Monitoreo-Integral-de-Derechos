package cl.smid.antecedentes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Punto de arranque del servicio SMID 6.8 (Antecedentes y Hallazgos). {@code @ConfigurationPropertiesScan}
 * registra los {@code @ConfigurationProperties} del paquete {@code config} (JWT y seguridad).
 */
@SpringBootApplication
@ConfigurationPropertiesScan("cl.smid.antecedentes.config")
public class AntecedentesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntecedentesApplication.class, args);
    }
}

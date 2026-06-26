package cl.smid.instituciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Punto de arranque del microservicio <strong>instituciones-service</strong> (SMID 6.10).
 *
 * <p>Servicio hoja del ecosistema: mantiene el catálogo nacional de instituciones,
 * sus tipos de clasificación y sus puntos focales. No invoca a otros servicios
 * (solo valida el JWT emitido por Identidad 6.1) y emite eventos de dominio
 * metadata-only con transporte conmutable (log por defecto / RabbitMQ).</p>
 *
 * <p>El esquema lo gobierna Flyway y Hibernate únicamente lo valida
 * ({@code ddl-auto=validate}); el dominio es POJO puro y se cablea en
 * {@link cl.smid.instituciones.config.CableadoDominio}.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class InstitucionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstitucionesApplication.class, args);
    }
}

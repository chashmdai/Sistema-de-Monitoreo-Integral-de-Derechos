package cl.smid.requerimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio requerimientos-service (Núcleo SMID 6.3).
 *
 * <p>Servicio de la Defensoría de los Derechos de la Niñez (Chile). Gestiona el ciclo de vida del
 * requerimiento (ingreso y admisibilidad) bajo arquitectura hexagonal: el dominio es POJO puro y
 * toda dependencia tecnológica vive en los adaptadores. Arranca con base de datos vacía; Flyway
 * solo crea el esquema (sin semilla de negocio).</p>
 */
@SpringBootApplication
public class RequerimientosApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequerimientosApplication.class, args);
    }
}

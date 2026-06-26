package cl.smid.catalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del servicio 6.7 — Catálogo de Derechos.
 *
 * <p>El catálogo es un servicio de <b>dato de referencia</b>: lectura intensiva y
 * escritura administrativa, sin datos personales. Corre en el puerto {@code 8087},
 * detrás del API Gateway que lo expone como {@code /api/catalogo/**} y aplica
 * {@code StripPrefix} de {@code /api}, por lo que internamente los controladores
 * cuelgan de {@code /catalogo/**}.</p>
 *
 * <p>La autoconfiguración de Spring Boot levanta: Flyway (que es el ÚNICO dueño del
 * esquema), Hibernate en modo {@code validate} (jamás {@code update}, cierra DT-5),
 * la cadena de seguridad que revalida el JWT por su cuenta (defensa en profundidad,
 * DT-3) y el composition root del dominio (ver {@code config.DominioConfig}).</p>
 */
@SpringBootApplication
public class CatalogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogoApplication.class, args);
    }
}

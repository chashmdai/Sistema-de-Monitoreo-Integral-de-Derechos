package cl.smid.casos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque de <strong>casos-service (SMID 6.4)</strong>.
 *
 * <p>Servicio del Núcleo Fundacional SMID (Defensoría de los Derechos de la Niñez). Es el
 * <em>agregado raíz</em> del expediente: en la arquitectura objetivo, "cuando un requerimiento se
 * asigna a un profesional, NACE un Caso". Este servicio es el primer consumidor de eventos
 * asíncronos del clúster: materializa un Caso por cada evento {@code requerimiento.asignado}.</p>
 *
 * <p><strong>Arranque sin migración.</strong> La base de datos arranca VACÍA: Flyway crea solo el
 * esquema, sin semilla de negocio ni migración de expedientes históricos. El historial permanece
 * congelado en el sistema legado (siger) y solo es accesible por el puente {@code siger-service}.
 * Los casos se materializan exclusivamente desde el evento entrante en adelante.</p>
 */
@SpringBootApplication
public class CasosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasosServiceApplication.class, args);
    }
}

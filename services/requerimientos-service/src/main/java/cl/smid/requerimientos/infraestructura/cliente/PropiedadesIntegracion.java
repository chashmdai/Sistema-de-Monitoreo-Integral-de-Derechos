package cl.smid.requerimientos.infraestructura.cliente;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de integración servicio a servicio (prefijo {@code smid.integracion}).
 *
 * @param personasUrl            URL base de personas-service (6.2)
 * @param catalogoUrl            URL base de catalogo-service (6.7)
 * @param identidadUrl           URL base de Identidad (6.1)
 * @param verificacionProfesional política de verificación del profesional: {@code permisiva}
 *                               (por defecto, tolera la indisponibilidad de Identidad) o
 *                               {@code estricta}
 */
@ConfigurationProperties(prefix = "smid.integracion")
public record PropiedadesIntegracion(
        String personasUrl,
        String catalogoUrl,
        String identidadUrl,
        String verificacionProfesional) {

    /** @return {@code true} si la verificación del profesional es estricta. */
    public boolean verificacionEstricta() {
        return "estricta".equalsIgnoreCase(verificacionProfesional);
    }
}

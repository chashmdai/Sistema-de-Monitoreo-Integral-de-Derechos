package cl.smid.catalogo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Propiedades de autorización del servicio (prefijo {@code smid.seguridad}).
 *
 * <p>El catálogo no es territorial: cualquier usuario autenticado puede leer, pero solo quienes
 * tengan <b>rol de Administración</b> pueden escribir. Como el nombre exacto del rol puede
 * variar entre entornos del ecosistema, se hace configurable: aquí se listan los roles que se
 * consideran «de Administración». La capa de seguridad exige que el usuario tenga al menos uno
 * de ellos para las operaciones de escritura.</p>
 */
@ConfigurationProperties(prefix = "smid.seguridad")
@Getter
@Setter
public class PropiedadesSeguridad {

    /**
     * Roles que confieren privilegios de Administración (escritura). Se proveen valores por
     * defecto razonables; en cada entorno pueden ajustarse vía configuración.
     */
    private List<String> rolesAdministracion = List.of(
            "ADMIN_NACIONAL", "ADMIN_CATALOGO", "ADMINISTRADOR");
}

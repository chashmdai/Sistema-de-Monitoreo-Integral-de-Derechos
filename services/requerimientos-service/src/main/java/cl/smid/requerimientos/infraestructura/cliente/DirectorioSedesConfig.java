package cl.smid.requerimientos.infraestructura.cliente;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioSedes;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Adaptador del puerto {@link DirectorioSedes} respaldado por configuración (prefijo
 * {@code smid.sedes}). Resuelve el código institucional de una sede (p. ej. {@code RM}) desde el
 * mapa {@code smid.sedes.codigos.<alt_key>}, con un respaldo determinista
 * ({@code smid.sedes.codigo-por-defecto}).
 *
 * <p>Decisión documentada: el token solo transporta el alt_key de la sede; el código institucional
 * no viaja en los claims y no hay endpoint estable de Identidad para resolverlo. Esta clase es la
 * costura: a futuro puede reemplazarse por un adaptador REST sin tocar el dominio. Respeta la regla
 * de arranque sin semilla de negocio (es configuración operativa, no datos sembrados en la BD).</p>
 */
@Component
@ConfigurationProperties(prefix = "smid.sedes")
public class DirectorioSedesConfig implements DirectorioSedes {

    /** Mapa alt_key de sede -> código institucional. */
    private Map<String, String> codigos = new HashMap<>();
    /** Respaldo determinista cuando no hay mapeo para la sede. */
    private String codigoPorDefecto = "SED";

    @Override
    public String codigoDeSede(String idSedeAlt) {
        if (idSedeAlt == null) {
            return codigoPorDefecto;
        }
        return codigos.getOrDefault(idSedeAlt, codigoPorDefecto);
    }

    // --- Enlazadores de configuración (setters requeridos por @ConfigurationProperties) ---

    public Map<String, String> getCodigos() {
        return codigos;
    }

    public void setCodigos(Map<String, String> codigos) {
        this.codigos = (codigos == null) ? new HashMap<>() : codigos;
    }

    public String getCodigoPorDefecto() {
        return codigoPorDefecto;
    }

    public void setCodigoPorDefecto(String codigoPorDefecto) {
        if (codigoPorDefecto != null && !codigoPorDefecto.isBlank()) {
            this.codigoPorDefecto = codigoPorDefecto;
        }
    }
}

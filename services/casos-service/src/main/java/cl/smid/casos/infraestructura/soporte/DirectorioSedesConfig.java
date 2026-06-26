package cl.smid.casos.infraestructura.soporte;

import cl.smid.casos.config.PropiedadesSedes;
import cl.smid.casos.dominio.puerto.salida.DirectorioSedes;
import org.springframework.stereotype.Component;

/**
 * Resuelve el código corto de sede a partir de su {@code alt_key} usando el mapa de configuración
 * {@code smid.sedes.codigos}. Si no hay mapeo, devuelve el código de respaldo
 * {@code smid.sedes.codigo-defecto}. Costura para un futuro adaptador contra Identidad (6.1).
 */
@Component
public class DirectorioSedesConfig implements DirectorioSedes {

    private final PropiedadesSedes propiedades;

    public DirectorioSedesConfig(PropiedadesSedes propiedades) {
        this.propiedades = propiedades;
    }

    @Override
    public String codigoDe(String idSedeAlt) {
        if (idSedeAlt == null) {
            return propiedades.codigoDefecto();
        }
        return propiedades.codigos().getOrDefault(idSedeAlt, propiedades.codigoDefecto());
    }
}

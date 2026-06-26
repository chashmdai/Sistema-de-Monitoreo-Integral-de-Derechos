package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioSedes;

import java.util.HashMap;
import java.util.Map;

/** Directorio de sedes falso: mapea alt_key de sede a código institucional (def. {@code RM}). */
public class DirectorioSedesFalso implements DirectorioSedes {

    private final Map<String, String> codigos = new HashMap<>();
    private String porDefecto = "RM";

    public DirectorioSedesFalso registrar(String idSedeAlt, String codigo) {
        codigos.put(idSedeAlt, codigo);
        return this;
    }

    public DirectorioSedesFalso porDefecto(String codigo) {
        this.porDefecto = codigo;
        return this;
    }

    @Override
    public String codigoDeSede(String idSedeAlt) {
        return codigos.getOrDefault(idSedeAlt, porDefecto);
    }
}

package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.ProfesionalIdentidad;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Directorio de Identidad falso: profesionales con su unidad, por alt_key. */
public class DirectorioIdentidadFalso implements DirectorioIdentidad {

    private final Map<String, ProfesionalIdentidad> profesionales = new HashMap<>();

    public DirectorioIdentidadFalso registrar(String altKey, String idUnidadAlt) {
        profesionales.put(altKey, new ProfesionalIdentidad(altKey, idUnidadAlt));
        return this;
    }

    @Override
    public Optional<ProfesionalIdentidad> resolverProfesional(String altKey) {
        return Optional.ofNullable(profesionales.get(altKey));
    }
}

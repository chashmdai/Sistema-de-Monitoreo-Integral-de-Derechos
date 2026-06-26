package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioPersonas;
import cl.smid.requerimientos.dominio.puerto.salida.PersonaResuelta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Directorio de personas falso: registra personas accesibles por alt_key. */
public class DirectorioPersonasFalso implements DirectorioPersonas {

    private final Map<String, PersonaResuelta> personas = new HashMap<>();

    public DirectorioPersonasFalso registrar(String altKey, String nombre, String rut) {
        personas.put(altKey, new PersonaResuelta(altKey, nombre, rut));
        return this;
    }

    @Override
    public Optional<PersonaResuelta> resolver(String altKey) {
        return Optional.ofNullable(personas.get(altKey));
    }
}

package cl.smid.requerimientos.infraestructura.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.GeneradorAltKey;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adaptador del puerto {@link GeneradorAltKey} que produce UUID v4 en formato canónico (36
 * caracteres). El alt_key es el identificador público; la PK interna nunca se expone (Núcleo 2.2).
 */
@Component
public class GeneradorAltKeyUuid implements GeneradorAltKey {

    @Override
    public String nuevo() {
        return UUID.randomUUID().toString();
    }
}

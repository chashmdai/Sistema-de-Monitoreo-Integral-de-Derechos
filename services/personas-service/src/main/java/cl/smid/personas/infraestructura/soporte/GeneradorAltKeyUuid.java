package cl.smid.personas.infraestructura.soporte;

import cl.smid.personas.dominio.puerto.salida.GeneradorAltKey;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adaptador del puerto {@link GeneradorAltKey} que produce UUID v4 aleatorios en su forma
 * canónica de 36 caracteres, usados como identificador público opaco (cierre de IDOR).
 */
@Component
public class GeneradorAltKeyUuid implements GeneradorAltKey {

    @Override
    public String nuevo() {
        return UUID.randomUUID().toString();
    }
}

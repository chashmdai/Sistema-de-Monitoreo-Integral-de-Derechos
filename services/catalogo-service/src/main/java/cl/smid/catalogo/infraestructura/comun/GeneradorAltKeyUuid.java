package cl.smid.catalogo.infraestructura.comun;

import cl.smid.catalogo.dominio.puerto.salida.GeneradorAltKey;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Implementación de producción del {@link GeneradorAltKey}: produce un UUID v4 aleatorio en
 * su forma canónica de 36 caracteres, que es el identificador público opaco del ecosistema.
 */
@Component
public class GeneradorAltKeyUuid implements GeneradorAltKey {

    @Override
    public String nuevo() {
        return UUID.randomUUID().toString();
    }
}

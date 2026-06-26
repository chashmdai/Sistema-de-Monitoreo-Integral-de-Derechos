package cl.smid.instituciones.infraestructura.soporte;

import cl.smid.instituciones.dominio.puerto.salida.GeneradorIdentificadores;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Implementación del puerto {@link GeneradorIdentificadores} que produce identificadores
 * públicos opacos como UUID versión 4.
 */
@Component
public class GeneradorUuid implements GeneradorIdentificadores {

    @Override
    public String nuevoAltKey() {
        return UUID.randomUUID().toString();
    }
}

package cl.smid.casos.infraestructura.soporte;

import cl.smid.casos.dominio.puerto.salida.GeneradorIdentificadores;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Generador de identificadores opacos basado en UUID v4. */
@Component
public class GeneradorUuid implements GeneradorIdentificadores {

    @Override
    public String nuevoAltKey() {
        return UUID.randomUUID().toString();
    }
}

package cl.smid.antecedentes.infraestructura.soporte;

import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Implementacion de {@link GeneradorIdentificadores} basada en {@link UUID#randomUUID()}
 * (UUID v4 aleatorio en formato canonico de 36 caracteres).
 */
@Component
public class GeneradorUuid implements GeneradorIdentificadores {

    @Override
    public String nuevoAltKey() {
        return UUID.randomUUID().toString();
    }
}

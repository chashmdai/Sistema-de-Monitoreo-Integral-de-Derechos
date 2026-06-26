package cl.smid.casos.infraestructura.soporte;

import cl.smid.casos.dominio.puerto.salida.Reloj;
import org.springframework.stereotype.Component;

import java.time.Instant;

/** Reloj del sistema en UTC. */
@Component
public class RelojSistema implements Reloj {

    @Override
    public Instant ahora() {
        return Instant.now();
    }
}

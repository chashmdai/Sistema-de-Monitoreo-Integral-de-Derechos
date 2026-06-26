package cl.smid.antecedentes.infraestructura.soporte;

import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Implementacion de produccion de {@link RelojDominio}: devuelve el instante actual del
 * sistema (UTC). En pruebas se sustituye por un reloj fijo.
 */
@Component
public class RelojSistema implements RelojDominio {

    @Override
    public Instant ahora() {
        return Instant.now();
    }
}

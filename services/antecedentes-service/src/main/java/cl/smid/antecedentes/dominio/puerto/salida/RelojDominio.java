package cl.smid.antecedentes.dominio.puerto.salida;

import java.time.Instant;

/**
 * Puerto de salida del reloj. Permite controlar el tiempo en pruebas. La implementacion de
 * produccion devuelve el instante actual del sistema (UTC).
 */
public interface RelojDominio {

    Instant ahora();
}

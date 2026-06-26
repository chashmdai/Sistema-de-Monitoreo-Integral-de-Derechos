package cl.smid.catalogo.dominio.puerto.salida;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Puerto de salida para el tiempo. Inyectar el reloj (en vez de llamar a
 * {@code LocalDate.now()} dentro del dominio) mantiene la lógica de negocio
 * <b>determinista y verificable</b>: las pruebas usan un reloj fijo.
 *
 * <p>Toda fecha/hora se maneja en <b>UTC</b> (Núcleo 2.10).</p>
 */
public interface RelojDominio {

    /** Fecha actual (UTC), usada como {@code vigente_desde} / {@code vigente_hasta}. */
    LocalDate hoy();

    /** Instante actual (UTC), usado para el {@code ocurrido_en} de los eventos. */
    Instant ahora();
}

package cl.smid.casos.dominio.puerto.entrada;

import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.EventoRequerimientoAsignado;

/**
 * Caso de uso: materializar un Caso a partir del evento {@code requerimiento.asignado}.
 *
 * <p>Es <strong>idempotente</strong>: ante la reentrega del mismo evento (entrega at-least-once)
 * garantiza un único caso por requerimiento de origen y trata el duplicado como no-op.</p>
 */
public interface MaterializarCaso {

    /**
     * Materializa el caso (o lo deja intacto si ya existía). Devuelve el caso resultante; en el
     * camino de duplicado, devuelve el caso ya existente sin crear uno nuevo.
     */
    Caso materializar(EventoRequerimientoAsignado evento);
}

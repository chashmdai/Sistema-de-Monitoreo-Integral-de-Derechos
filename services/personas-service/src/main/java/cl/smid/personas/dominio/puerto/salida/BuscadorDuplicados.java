package cl.smid.personas.dominio.puerto.salida;

import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;

/**
 * Puerto de la prevalidación de duplicados (Núcleo 5.5). Encapsular la estrategia tras este
 * puerto es la <b>costura para el futuro</b>: en esta entrega el único adaptador coteja la base
 * local SMID ({@code BuscadorDuplicadosBaseLocal}); más adelante podrá añadirse un adaptador que
 * además consulte a {@code siger-service} (por RUT y por nombre) sin reescribir el dominio.
 */
public interface BuscadorDuplicados {

    /**
     * Busca coincidencias exactas (por RUT) y probables (difusas por nombre/fecha) para el
     * criterio dado. Sólo informa; no fusiona.
     *
     * @param criterio datos mínimos de búsqueda
     * @return coincidencia exacta opcional y lista ordenada de probables
     */
    ResultadoDuplicados buscar(CriterioDuplicados criterio);
}

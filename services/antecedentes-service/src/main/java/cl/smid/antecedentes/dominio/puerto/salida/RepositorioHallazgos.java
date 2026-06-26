package cl.smid.antecedentes.dominio.puerto.salida;

import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia de {@link Hallazgo}.
 */
public interface RepositorioHallazgos {

    /** Persiste (alta o actualizacion) el hallazgo y devuelve su estado persistido. */
    Hallazgo guardar(Hallazgo hallazgo);

    /** Recupera un hallazgo por alt_key. */
    Optional<Hallazgo> buscarPorAltKey(String altKey);

    /** Indica si existe un hallazgo con ese alt_key (para validar coherencia de la ficha). */
    boolean existePorAltKey(String altKey);

    /** Lista hallazgos (lectura nacional) segun criterios. */
    Pagina<Hallazgo> buscar(FiltroHallazgos filtro);
}

package cl.smid.antecedentes.dominio.puerto.salida;

import cl.smid.antecedentes.dominio.modelo.CriterioTerritorial;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia de {@link FichaAntecedente}. El adaptador traduce
 * alt_key&lt;-&gt;id, cifra/descifra el relato y resuelve las referencias por su alt_key.
 */
public interface RepositorioFichas {

    /** Persiste (alta o actualizacion) la ficha y devuelve su estado persistido. */
    FichaAntecedente guardar(FichaAntecedente ficha);

    /** Recupera el agregado completo por alt_key (con relato descifrado). */
    Optional<FichaAntecedente> buscarPorAltKey(String altKey);

    /** Elimina la ficha por alt_key. */
    void eliminarPorAltKey(String altKey);

    /**
     * Lista resumenes de ficha acotados al alcance territorial y a los criterios funcionales.
     * No descifra el relato ni carga colecciones hijas.
     */
    Pagina<ResumenFicha> buscar(FiltroFichas filtro, CriterioTerritorial territorio);
}

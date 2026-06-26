package cl.smid.catalogo.dominio.puerto.salida;

import cl.smid.catalogo.dominio.modelo.Causa;

import java.util.List;

/**
 * Puerto de salida hacia la persistencia de <b>causas</b>.
 */
public interface CausaRepositorio {

    /** Causas vigentes de un derecho, en orden estable por código. */
    List<Causa> causasVigentesDe(Long idDerecho);

    /** {@code true} si el derecho ya tiene una causa con ese código (unicidad por derecho). */
    boolean existeCodigoEnDerecho(Long idDerecho, String codigo);

    /** Inserta una causa nueva y devuelve la instancia con su {@code id} interno asignado. */
    Causa guardarNueva(Causa causa);
}

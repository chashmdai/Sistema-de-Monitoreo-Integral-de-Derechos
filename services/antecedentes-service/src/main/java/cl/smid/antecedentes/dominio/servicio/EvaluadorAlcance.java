package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;

import java.util.Objects;

/**
 * Evalua el acceso territorial a una ficha segun el alcance del solicitante (Nucleo 2.3). Es un
 * POJO sin estado. La denegacion se traduce en 404 (no 403) en la capa de servicio.
 */
public class EvaluadorAlcance {

    /**
     * Indica si el solicitante puede <strong>ver</strong> la ficha:
     * {@code NACIONAL} ve todo; {@code SEDE} ve su sede; {@code UNIDAD} ve su unidad.
     */
    public boolean puedeVer(ContextoSesion contexto, FichaAntecedente ficha) {
        return switch (contexto.alcance()) {
            case NACIONAL -> true;
            case SEDE -> Objects.equals(contexto.idSede(), ficha.sedeAlt());
            case UNIDAD -> Objects.equals(contexto.idUnidad(), ficha.unidadAlt());
        };
    }

    /**
     * Indica si el solicitante puede <strong>editar/eliminar</strong> la ficha: debe pertenecer
     * a la misma unidad (mismo centro), independientemente de su alcance de lectura.
     */
    public boolean perteneceMismaUnidad(ContextoSesion contexto, FichaAntecedente ficha) {
        return contexto.idUnidad() != null && Objects.equals(contexto.idUnidad(), ficha.unidadAlt());
    }
}

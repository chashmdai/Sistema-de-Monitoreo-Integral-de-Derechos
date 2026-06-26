package cl.smid.casos.dominio.excepcion;

import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.EstadoCaso;

/**
 * Se intentó una transición no contemplada por la máquina de estados para el estado actual
 * del caso. Responde {@code 409} (CAS-409).
 */
public class TransicionInvalidaException extends ExcepcionDominio {

    public TransicionInvalidaException(EstadoCaso origen, AccionCaso accion) {
        super(CodigoError.CAS_409,
                "La acción '" + accion + "' no es válida desde el estado '" + origen + "'.");
    }
}

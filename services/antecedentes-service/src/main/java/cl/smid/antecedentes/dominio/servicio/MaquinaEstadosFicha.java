package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;
import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;

import java.util.Map;

/**
 * Maquina de estados de la {@link cl.smid.antecedentes.dominio.modelo.FichaAntecedente}.
 * Tabla de transiciones pura {@code (origen, accion) -> destino}, resolucion O(1). Es un POJO
 * sin estado ni dependencias de framework.
 *
 * <pre>
 *   BORRADOR   --ENVIAR_REVISION--&gt; EN_REVISION
 *   EN_REVISION --DEVOLVER--------&gt; BORRADOR
 *   EN_REVISION --APROBAR---------&gt; APROBADA  (terminal)
 *   EN_REVISION --RECHAZAR--------&gt; RECHAZADA (terminal)
 * </pre>
 *
 * Una transicion no contemplada lanza {@code ANT-422}.
 */
public class MaquinaEstadosFicha {

    private record Clave(EstadoFicha origen, AccionRevision accion) {
    }

    private static final Map<Clave, EstadoFicha> TRANSICIONES = Map.of(
            new Clave(EstadoFicha.BORRADOR, AccionRevision.ENVIAR_REVISION), EstadoFicha.EN_REVISION,
            new Clave(EstadoFicha.EN_REVISION, AccionRevision.DEVOLVER), EstadoFicha.BORRADOR,
            new Clave(EstadoFicha.EN_REVISION, AccionRevision.APROBAR), EstadoFicha.APROBADA,
            new Clave(EstadoFicha.EN_REVISION, AccionRevision.RECHAZAR), EstadoFicha.RECHAZADA
    );

    /**
     * Calcula el estado destino de aplicar una accion sobre un estado dado.
     *
     * @param origen estado actual de la ficha
     * @param accion accion de revision solicitada
     * @return estado destino valido
     * @throws ReglaNegocioException {@code ANT-422} si la transicion no es valida
     */
    public EstadoFicha siguiente(EstadoFicha origen, AccionRevision accion) {
        EstadoFicha destino = TRANSICIONES.get(new Clave(origen, accion));
        if (destino == null) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Transicion invalida: no se puede aplicar " + accion + " sobre una ficha en estado " + origen + ".");
        }
        return destino;
    }
}

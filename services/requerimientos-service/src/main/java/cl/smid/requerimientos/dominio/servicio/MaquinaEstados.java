package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.excepcion.TransicionInvalida;
import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;

import java.util.Map;

/**
 * Máquina de estados del requerimiento (Núcleo 6.3), modelada como una <b>tabla pura de
 * transición</b> (estado, evento) → estado, no como condicionales dispersos.
 *
 * <p>Grafo de estados:</p>
 * <pre>
 *   BORRADOR --ENVIAR--&gt; INGRESADO --ABRIR_ADMISIBILIDAD--&gt; EN_ADMISIBILIDAD
 *       EN_ADMISIBILIDAD --DECIDIR_INADMISIBLE---------&gt; INADMISIBLE
 *       EN_ADMISIBILIDAD --DECIDIR_RESPUESTA_INMEDIATA-&gt; RESPONDIDO
 *       EN_ADMISIBILIDAD --DECIDIR_ASIGNACION----------&gt; ASIGNADO
 * </pre>
 *
 * <p>Cualquier par no contemplado en la tabla es una transición inválida y produce
 * {@link TransicionInvalida} (REQ-409). Es dominio puro: sin estado mutable ni dependencias
 * de framework; todos sus métodos son funciones puras.</p>
 */
public final class MaquinaEstados {

    /**
     * Clave de la tabla de transición: el par (estado de origen, evento).
     *
     * @param origen estado de partida
     * @param evento evento disparador
     */
    private record Clave(EstadoRequerimiento origen, EventoTransicion evento) {
    }

    /**
     * Tabla de transición inmutable: (origen, evento) → destino. La ausencia de una clave
     * significa "transición no permitida".
     */
    private static final Map<Clave, EstadoRequerimiento> TABLA = Map.of(
            new Clave(EstadoRequerimiento.BORRADOR, EventoTransicion.ENVIAR),
            EstadoRequerimiento.INGRESADO,

            new Clave(EstadoRequerimiento.INGRESADO, EventoTransicion.ABRIR_ADMISIBILIDAD),
            EstadoRequerimiento.EN_ADMISIBILIDAD,

            new Clave(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_INADMISIBLE),
            EstadoRequerimiento.INADMISIBLE,

            new Clave(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_RESPUESTA_INMEDIATA),
            EstadoRequerimiento.RESPONDIDO,

            new Clave(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_ASIGNACION),
            EstadoRequerimiento.ASIGNADO
    );

    private MaquinaEstados() {
        // Utilidad estática: no se instancia.
    }

    /**
     * Calcula el estado siguiente al aplicar un evento sobre un estado dado.
     *
     * @param actual estado actual del requerimiento
     * @param evento evento que se intenta aplicar
     * @return el estado resultante
     * @throws TransicionInvalida si el par (actual, evento) no es una arista válida (REQ-409)
     */
    public static EstadoRequerimiento siguiente(EstadoRequerimiento actual, EventoTransicion evento) {
        EstadoRequerimiento destino = TABLA.get(new Clave(actual, evento));
        if (destino == null) {
            throw new TransicionInvalida(
                    "Transición no permitida: no se puede aplicar el evento " + evento
                            + " sobre un requerimiento en estado " + actual + ".");
        }
        return destino;
    }

    /**
     * Indica si un evento es aplicable sobre un estado, sin lanzar excepción.
     *
     * @param actual estado actual
     * @param evento evento a evaluar
     * @return {@code true} si existe una arista válida (actual, evento)
     */
    public static boolean puedeTransitar(EstadoRequerimiento actual, EventoTransicion evento) {
        return TABLA.containsKey(new Clave(actual, evento));
    }

    /**
     * Traduce la acción de admisibilidad (USR.02) al evento de transición correspondiente.
     *
     * @param accion acción decidida por Coordinación
     * @return el evento de transición asociado
     */
    public static EventoTransicion eventoDeAccion(AccionAdmisibilidad accion) {
        return switch (accion) {
            case INADMISIBLE -> EventoTransicion.DECIDIR_INADMISIBLE;
            case RESPUESTA_INMEDIATA -> EventoTransicion.DECIDIR_RESPUESTA_INMEDIATA;
            case ASIGNACION -> EventoTransicion.DECIDIR_ASIGNACION;
        };
    }
}

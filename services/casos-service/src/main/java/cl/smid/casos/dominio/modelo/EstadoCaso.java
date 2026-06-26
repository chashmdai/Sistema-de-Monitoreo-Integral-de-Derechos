package cl.smid.casos.dominio.modelo;

/**
 * Estados posibles del ciclo de vida de un Caso (expediente).
 *
 * <p>No existe especificación formal de 6.4 en la documentación del Núcleo (Casos se declara fuera
 * de alcance del núcleo, §1.3). Por ello, esta máquina se DISEÑA de forma coherente con el evento
 * entrante {@code requerimiento.asignado}, los principios del capítulo 2 y las costuras dejadas por
 * requerimientos. La decisión queda declarada en la cabecera de {@code V1__inicial.sql} y el README.</p>
 *
 * <ul>
 *   <li>{@link #ABIERTO}: estado inicial al materializar el caso desde el evento ("esqueleto").</li>
 *   <li>{@link #EN_INVESTIGACION}: el profesional responsable trabaja activamente el expediente.</li>
 *   <li>{@link #EN_SEGUIMIENTO}: fase de monitoreo posterior a la investigación.</li>
 *   <li>{@link #SUSPENDIDO}: pausa temporal (a la espera de antecedentes, derivaciones, etc.).</li>
 *   <li>{@link #CERRADO}: cierre del expediente; cuasi-terminal (reabrible o archivable).</li>
 *   <li>{@link #ARCHIVADO}: terminal definitivo; no admite más transiciones.</li>
 * </ul>
 */
public enum EstadoCaso {
    ABIERTO,
    EN_INVESTIGACION,
    EN_SEGUIMIENTO,
    SUSPENDIDO,
    CERRADO,
    ARCHIVADO
}

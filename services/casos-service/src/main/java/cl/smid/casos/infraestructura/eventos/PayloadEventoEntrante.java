package cl.smid.casos.infraestructura.eventos;

import cl.smid.casos.dominio.modelo.Complejidad;
import cl.smid.casos.dominio.modelo.EventoRequerimientoAsignado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

/**
 * DTO de transporte del evento entrante {@code requerimiento.asignado}, tal como lo publica
 * requerimientos-service. Deserialización TOLERANTE: se ignoran campos desconocidos para no romper
 * ante evoluciones del contrato del productor.
 *
 * <p>Estructura esperada:</p>
 * <pre>
 * {
 *   "tipo": "requerimiento.asignado",
 *   "altKey": "&lt;alt_key del requerimiento de origen&gt;",
 *   "ocurridoEn": "&lt;ISO-8601 UTC&gt;",
 *   "metadatos": {
 *     "folio", "estado", "idSede", "idUnidadDestino", "complejidad",
 *     "requiereFichaReservada", "esBeta", "accion", "idProfesionalAsignadoAlt"
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PayloadEventoEntrante(String tipo, String altKey, Instant ocurridoEn,
                                    Metadatos metadatos) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Metadatos(String folio, String estado, String idSede, String idUnidadDestino,
                            String complejidad, Boolean requiereFichaReservada, Boolean esBeta,
                            String accion, String idProfesionalAsignadoAlt) {
    }

    /**
     * Traduce el payload al evento de dominio, validando lo mínimo imprescindible (alt_key de origen y
     * de sede). Campos opcionales se interpretan con tolerancia.
     *
     * @throws IllegalArgumentException si faltan datos obligatorios (el listener lo enruta a DLQ).
     */
    public EventoRequerimientoAsignado aDominio() {
        if (altKey == null || altKey.isBlank()) {
            throw new IllegalArgumentException("El evento no trae 'altKey' del requerimiento de origen.");
        }
        if (metadatos == null) {
            throw new IllegalArgumentException("El evento no trae 'metadatos'.");
        }
        if (metadatos.idSede() == null || metadatos.idSede().isBlank()) {
            throw new IllegalArgumentException("El evento no trae 'metadatos.idSede'.");
        }
        Instant ocurrido = (ocurridoEn != null) ? ocurridoEn : Instant.now();
        boolean fichaReservada = Boolean.TRUE.equals(metadatos.requiereFichaReservada());
        return new EventoRequerimientoAsignado(
                altKey,
                ocurrido,
                metadatos.folio(),
                metadatos.idSede(),
                metadatos.idUnidadDestino(),
                interpretarComplejidad(metadatos.complejidad()),
                fichaReservada,
                metadatos.esBeta(),
                metadatos.idProfesionalAsignadoAlt());
    }

    /** Interpreta la complejidad de forma tolerante: valor desconocido o ausente ⇒ {@code null}. */
    private static Complejidad interpretarComplejidad(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Complejidad.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

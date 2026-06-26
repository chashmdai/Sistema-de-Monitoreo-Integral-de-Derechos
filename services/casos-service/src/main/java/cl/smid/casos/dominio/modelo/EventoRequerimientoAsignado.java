package cl.smid.casos.dominio.modelo;

import java.time.Instant;
import java.util.Objects;

/**
 * Representación de dominio del evento entrante {@code requerimiento.asignado}, ya validada y
 * traducida desde el payload de transporte. Contiene EXCLUSIVAMENTE referencias {@code alt_key} y
 * metadatos no sensibles (G7: sin nombres ni RUT), que es justamente lo que permite la estrategia de
 * materialización "esqueleto".
 *
 * @param requerimientoOrigenAlt alt_key del requerimiento de origen (clave de idempotencia).
 * @param ocurridoEn             instante en que ocurrió la asignación (UTC).
 * @param folio                  folio del requerimiento (metadato).
 * @param idSedeAlt              alt_key de la sede.
 * @param idUnidadDestinoAlt     alt_key de la unidad de destino.
 * @param complejidad            complejidad del requerimiento (puede ser nula).
 * @param requiereFichaReservada bandera FIR; costura hacia Vulneraciones (6.5).
 * @param esBeta                 si el requerimiento pertenece a la serie BETA; puede ser nula.
 * @param idProfesionalResponsableAlt alt_key del profesional asignado (responsable del caso).
 */
public record EventoRequerimientoAsignado(String requerimientoOrigenAlt, Instant ocurridoEn,
                                          String folio, String idSedeAlt, String idUnidadDestinoAlt,
                                          Complejidad complejidad, boolean requiereFichaReservada,
                                          Boolean esBeta, String idProfesionalResponsableAlt) {

    public EventoRequerimientoAsignado {
        Objects.requireNonNull(requerimientoOrigenAlt, "requerimientoOrigenAlt");
        Objects.requireNonNull(idSedeAlt, "idSedeAlt");
        if (requerimientoOrigenAlt.isBlank()) {
            throw new IllegalArgumentException("El evento no trae alt_key de requerimiento de origen.");
        }
        if (idSedeAlt.isBlank()) {
            throw new IllegalArgumentException("El evento no trae alt_key de sede.");
        }
    }
}

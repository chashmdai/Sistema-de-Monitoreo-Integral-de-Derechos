package cl.smid.catalogo.dominio.puerto.salida;

import java.time.Instant;
import java.util.Map;

/**
 * Evento de dominio del catálogo, con el formato del ecosistema (Núcleo 2.8).
 *
 * <p>Regla de la carga útil (G7): los eventos llevan <b>identificadores opacos y
 * metadatos</b>, jamás contenido sensible. En el catálogo no hay datos personales, de
 * modo que esto se cumple trivialmente. Los eventos quedan disponibles para el futuro
 * servicio de Auditoría sin reescribir nada (costura).</p>
 *
 * @param evento     nombre del hecho de dominio (p. ej. {@code "catalogo.derecho.baja"})
 * @param ocurridoEn instante del hecho (UTC)
 * @param actor      alt_key del usuario que originó el hecho (del claim {@code sub})
 * @param recurso    alt_key del recurso afectado (el derecho/causa)
 * @param idSede     alt_key de la sede del actor (claim, puede ser nulo)
 * @param idUnidad   alt_key de la unidad del actor (claim, puede ser nulo)
 * @param datos      metadatos adicionales (p. ej. {@code descendientesAfectados}); nunca datos sensibles
 */
public record EventoDominio(
        String evento,
        Instant ocurridoEn,
        String actor,
        String recurso,
        String idSede,
        String idUnidad,
        Map<String, Object> datos
) {
    public EventoDominio {
        datos = (datos == null) ? Map.of() : Map.copyOf(datos);
    }
}

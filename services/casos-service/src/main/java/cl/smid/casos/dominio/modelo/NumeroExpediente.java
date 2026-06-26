package cl.smid.casos.dominio.modelo;

import java.util.Objects;

/**
 * Objeto de valor del número de expediente (análogo al folio de requerimientos).
 *
 * <p>Formato: {@code EXP-{CODIGO_SEDE}-{[B]CORRELATIVO}/{AÑO}}.
 * Ejemplos: {@code EXP-RM-1/2027} (oficial), {@code EXP-RM-B1/2027} (beta).</p>
 *
 * <p>El prefijo {@code B} del correlativo aísla la serie BETA. El correlativo es atómico y único por
 * {@code (sede, año, serie)} (lo reserva el adaptador de persistencia mediante UPSERT MySQL).</p>
 *
 * @param valor       representación textual completa, persistida en {@code caso.numero_expediente}.
 * @param codigoSede  código corto de la sede (p. ej. {@code RM}); resuelto por configuración.
 * @param serie       OFICIAL o BETA.
 * @param correlativo número secuencial dentro de {@code (sede, año, serie)}.
 * @param anio        año al que pertenece la serie.
 */
public record NumeroExpediente(String valor, String codigoSede, SerieExpediente serie,
                               long correlativo, int anio) {

    public NumeroExpediente {
        Objects.requireNonNull(valor, "valor");
        Objects.requireNonNull(codigoSede, "codigoSede");
        Objects.requireNonNull(serie, "serie");
        if (correlativo <= 0) {
            throw new IllegalArgumentException("El correlativo del expediente debe ser positivo.");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}

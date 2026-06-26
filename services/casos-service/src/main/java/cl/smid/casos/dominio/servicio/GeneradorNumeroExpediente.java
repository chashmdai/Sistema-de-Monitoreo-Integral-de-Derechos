package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.modelo.NumeroExpediente;
import cl.smid.casos.dominio.modelo.SerieExpediente;

/**
 * Compone el número de expediente a partir de sus piezas, aislando la serie BETA con el prefijo
 * {@code B} en el correlativo. La reserva atómica del correlativo la realiza el adaptador de
 * persistencia (puerto {@code CorrelativoExpedientePort}); aquí solo se da forma al valor.
 *
 * <p>Formato: {@code EXP-{CODIGO_SEDE}-{[B]CORRELATIVO}/{AÑO}}
 * (p. ej. {@code EXP-RM-1/2027} oficial, {@code EXP-RM-B1/2027} beta).</p>
 */
public final class GeneradorNumeroExpediente {

    public NumeroExpediente generar(String codigoSede, SerieExpediente serie, long correlativo, int anio) {
        String prefijoSerie = (serie == SerieExpediente.BETA) ? "B" : "";
        String valor = "EXP-" + codigoSede + "-" + prefijoSerie + correlativo + "/" + anio;
        return new NumeroExpediente(valor, codigoSede, serie, correlativo, anio);
    }
}

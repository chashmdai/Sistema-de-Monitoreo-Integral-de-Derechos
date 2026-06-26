package cl.smid.requerimientos.dominio.modelo;

import java.util.Objects;

/**
 * Objeto de valor que representa el folio oficial de un requerimiento (Núcleo 6.5).
 *
 * <p>Formato: <code>{CODIGO_SEDE}-{[B]CORRELATIVO}/{AÑO}</code>. La serie {@code BETA}
 * antepone el prefijo {@code B} al correlativo, de modo que jamás colisiona con la
 * serie oficial:</p>
 * <ul>
 *   <li>Oficial: {@code RM-1/2027}</li>
 *   <li>Beta:    {@code RM-B1/2027}</li>
 * </ul>
 *
 * <p>Inmutable. Pertenece al dominio puro.</p>
 *
 * @param valor representación textual ya compuesta del folio (máx. 24 caracteres en BD)
 */
public record Folio(String valor) {

    public Folio {
        Objects.requireNonNull(valor, "El folio no puede ser nulo");
        if (valor.isBlank()) {
            throw new IllegalArgumentException("El folio no puede estar en blanco");
        }
        if (valor.length() > 24) {
            throw new IllegalArgumentException("El folio excede 24 caracteres: " + valor);
        }
    }

    /**
     * Compone un folio a partir de sus componentes.
     *
     * @param codigoSede  código institucional de la sede (p. ej. {@code RM})
     * @param serie       serie de numeración (determina el prefijo)
     * @param correlativo número correlativo dentro de (sede, año, serie); &gt;= 1
     * @param anio        año de la serie
     * @return el folio compuesto
     */
    public static Folio componer(String codigoSede, SerieFolio serie, long correlativo, int anio) {
        Objects.requireNonNull(codigoSede, "El código de sede no puede ser nulo");
        Objects.requireNonNull(serie, "La serie no puede ser nula");
        if (correlativo < 1) {
            throw new IllegalArgumentException("El correlativo debe ser >= 1: " + correlativo);
        }
        String texto = codigoSede + "-" + serie.prefijo() + correlativo + "/" + anio;
        return new Folio(texto);
    }

    @Override
    public String toString() {
        return valor;
    }
}

package cl.smid.antecedentes.dominio.modelo;

import java.util.Objects;

/**
 * Objeto de valor que representa un folio ya compuesto.
 *
 * <p>Las fichas usan la serie {@code FA-{N}/{anio}} y los hallazgos la serie
 * {@code HZ-{N}/{anio}}; ambas son independientes y reinician anualmente. La
 * generacion atomica del correlativo vive en el adaptador de persistencia; este VO
 * solo encapsula el valor formateado e inmutable.</p>
 */
public final class Folio {

    private final String valor;

    public Folio(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El folio no puede ser nulo ni vacio");
        }
        this.valor = valor;
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Folio folio)) {
            return false;
        }
        return valor.equals(folio.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}

package cl.smid.personas.dominio.modelo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Resultado de la prevalidación de duplicados (Núcleo 5.5). Combina, si existe, la
 * coincidencia exacta por RUT y la lista —ya ordenada por {@code score} descendente— de
 * coincidencias probables por similitud de nombre/fecha.
 *
 * <p>El servicio sólo informa: nunca fusiona registros (esa decisión pertenece al flujo de
 * Requerimientos 6.4).</p>
 *
 * @param coincidenciaExacta   match por RUT, si lo hay
 * @param coincidenciasProbables candidatas difusas (lista inmutable, posiblemente vacía)
 */
public record ResultadoDuplicados(
        Optional<CoincidenciaExacta> coincidenciaExacta,
        List<CoincidenciaProbable> coincidenciasProbables
) {

    public ResultadoDuplicados {
        coincidenciaExacta = (coincidenciaExacta == null) ? Optional.empty() : coincidenciaExacta;
        coincidenciasProbables = (coincidenciasProbables == null)
                ? Collections.emptyList()
                : List.copyOf(coincidenciasProbables);
    }

    /** Resultado sin coincidencia alguna. */
    public static ResultadoDuplicados vacio() {
        return new ResultadoDuplicados(Optional.empty(), Collections.emptyList());
    }
}

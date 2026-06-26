package cl.smid.casos.dominio.puerto.entrada;

import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;

import java.util.Objects;

/**
 * Caso de uso: aplicar una transición de la máquina de estados sobre un caso existente.
 */
public interface TransicionarCaso {

    /**
     * Aplica la {@code accion} del comando sobre el caso indicado, validando alcance territorial,
     * facultad (rol de Coordinación para acciones administrativas) y validez de la transición.
     *
     * @return el caso con su estado actualizado.
     */
    Caso transicionar(ComandoTransicion comando, ContextoTerritorial ctx);

    /**
     * Comando de transición.
     *
     * @param casoAltKey  identificador opaco del caso a transicionar.
     * @param accion      acción a aplicar.
     * @param observacion nota opcional asociada a la transición.
     */
    record ComandoTransicion(String casoAltKey, AccionCaso accion, String observacion) {

        public ComandoTransicion {
            Objects.requireNonNull(casoAltKey, "casoAltKey");
            Objects.requireNonNull(accion, "accion");
        }
    }
}

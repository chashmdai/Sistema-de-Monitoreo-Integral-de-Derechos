package cl.smid.antecedentes.dominio.puerto.entrada;

import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;

import java.util.List;

/**
 * Caso de uso de gestion de hallazgos. Lectura nacional autenticada; la creacion/edicion y el
 * cambio de estado exigen rol de gestion/revisor.
 */
public interface GestionHallazgosUseCase {

    /** Crea una propuesta de hallazgo directa (rol gestion/revisor). */
    Hallazgo crear(ComandoCrear comando, ContextoSesion contexto);

    /** Obtiene el detalle de un hallazgo (lectura nacional). 404 si no existe. */
    Hallazgo obtener(String altKey, ContextoSesion contexto);

    /** Edita una propuesta (solo en estado PROPUESTO; rol gestion/revisor). */
    Hallazgo editar(String altKey, ComandoEditar comando, ContextoSesion contexto);

    /** Cambia el estado a ASOCIADO o RECHAZADO (rol gestion/revisor). */
    Hallazgo cambiarEstado(String altKey, ComandoCambiarEstado comando, ContextoSesion contexto);

    /** Lista hallazgos (lectura nacional) segun filtros. */
    Pagina<Hallazgo> listar(FiltroHallazgos filtro, ContextoSesion contexto);

    // -----------------------------------------------------------------------------------
    // Comandos
    // -----------------------------------------------------------------------------------

    /**
     * @param titulo                titulo del hallazgo
     * @param descripcion           descripcion
     * @param instrumentoId         alt_key del instrumento (obligatorio, vigente)
     * @param temporalidad          horizonte temporal
     * @param unidadesInvolucradas  alt_key de unidades involucradas
     * @param institucionesExternas alt_key de instituciones externas (6.10)
     */
    record ComandoCrear(
            String titulo,
            String descripcion,
            String instrumentoId,
            Temporalidad temporalidad,
            List<String> unidadesInvolucradas,
            List<String> institucionesExternas) {
    }

    /** Comando de edicion de propuesta (mismos campos que la creacion). */
    record ComandoEditar(
            String titulo,
            String descripcion,
            String instrumentoId,
            Temporalidad temporalidad,
            List<String> unidadesInvolucradas,
            List<String> institucionesExternas) {
    }

    /** Comando de cambio de estado (destino ASOCIADO o RECHAZADO). */
    record ComandoCambiarEstado(EstadoHallazgo estado) {
    }
}

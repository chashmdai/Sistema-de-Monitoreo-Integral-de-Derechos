package cl.smid.requerimientos.dominio.puerto.entrada;

import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import cl.smid.requerimientos.dominio.modelo.Alcance;
import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.modelo.Urgencia;

import java.util.List;
import java.util.Set;

/**
 * Puerto de entrada (caso de uso) de la gestión de requerimientos (Núcleo 6.3). Define las
 * operaciones del ciclo USR.01 (ingreso) y USR.02 (admisibilidad). La capa {@code api} lo invoca;
 * la implementación vive en {@code dominio.servicio} y orquesta los puertos de salida.
 *
 * <p>Las operaciones devuelven el agregado de dominio; el mapeo a DTO y la demarcación
 * transaccional son responsabilidad de la capa de aplicación. Todas las referencias externas
 * viajan como alt_key.</p>
 */
public interface GestionRequerimientosUseCase {

    /**
     * Crea un requerimiento en BORRADOR con datos parciales (USR.01). Asigna folio y estampa la
     * sede/autoría desde el contexto del usuario.
     *
     * @param cmd comando de creación
     * @param ctx contexto del usuario autenticado
     * @return el requerimiento creado
     */
    Requerimiento crear(ComandoCrear cmd, ContextoUsuario ctx);

    /**
     * Edita un requerimiento mutable (PUT, partial-merge). Aplica filtro territorial.
     *
     * @param altKey alt_key del requerimiento
     * @param cmd    comando de edición
     * @param ctx    contexto del usuario
     * @return el requerimiento editado
     */
    Requerimiento editar(String altKey, ComandoEditar cmd, ContextoUsuario ctx);

    /**
     * Envía un requerimiento: BORRADOR -> INGRESADO (valida mínimos). Aplica filtro territorial.
     *
     * @param altKey alt_key del requerimiento
     * @param ctx    contexto del usuario
     * @return el requerimiento ingresado
     */
    Requerimiento enviar(String altKey, ContextoUsuario ctx);

    /**
     * Agrega un NNA afectado con sus derechos vulnerados (USR.01). Verifica las referencias por
     * alt_key contra Personas y Catálogo. Aplica filtro territorial.
     *
     * @param altKey alt_key del requerimiento
     * @param cmd    comando de alta de NNA
     * @param ctx    contexto del usuario
     * @return el requerimiento con el NNA agregado
     */
    Requerimiento agregarNna(String altKey, ComandoAgregarNna cmd, ContextoUsuario ctx);

    /**
     * Registra la decisión de admisibilidad (USR.02). Exige rol de Coordinación (autorizado en la
     * capa de aplicación). Aplica filtro territorial.
     *
     * @param altKey alt_key del requerimiento
     * @param cmd    comando de decisión
     * @param ctx    contexto del usuario
     * @return el requerimiento con la decisión aplicada
     */
    Requerimiento decidirAdmisibilidad(String altKey, ComandoAdmisibilidad cmd, ContextoUsuario ctx);

    /**
     * Obtiene el detalle de un requerimiento. Fuera de alcance => 404 (no se revela existencia).
     *
     * @param altKey alt_key del requerimiento
     * @param ctx    contexto del usuario
     * @return el requerimiento
     */
    Requerimiento obtener(String altKey, ContextoUsuario ctx);

    /**
     * Lista requerimientos con filtros opcionales, dentro del alcance territorial del usuario.
     *
     * @param filtro criterios de listado
     * @param ctx    contexto del usuario
     * @return página de requerimientos visibles
     */
    PaginaDominio<Requerimiento> listar(FiltroListado filtro, ContextoUsuario ctx);

    // ========================================================================
    //  Comandos y contexto (records inmutables de entrada)
    // ========================================================================

    /**
     * Contexto del usuario autenticado, derivado de los claims del JWT (Núcleo 2.3).
     *
     * @param usuarioAlt alt_key del usuario (claim {@code sub})
     * @param idSedeAlt  alt_key de su sede (claim {@code idSede})
     * @param idUnidadAlt alt_key de su unidad (claim {@code idUnidad})
     * @param alcance    alcance territorial (claim {@code alcance})
     * @param roles      roles del usuario (claim {@code roles})
     */
    record ContextoUsuario(String usuarioAlt, String idSedeAlt, String idUnidadAlt,
                           Alcance alcance, Set<String> roles) {
        public ContextoUsuario {
            roles = (roles == null) ? Set.of() : Set.copyOf(roles);
        }
    }

    /**
     * Comando de creación de un requerimiento en BORRADOR.
     *
     * @param canal              canal de ingreso (nulable)
     * @param complejidad        complejidad inicial (nulable)
     * @param urgencia           urgencia inicial (nulable)
     * @param idUnidadDestinoAlt unidad de destino inicial (nulable)
     * @param resumen            resumen inicial (nulable)
     * @param idRequirenteAlt    alt_key del requirente (nulable; si viene, se resuelve snapshot)
     * @param esBetaOverride     override de serie de folio: {@code TRUE}=BETA, {@code FALSE}=OFICIAL,
     *                           {@code null}=política por fecha
     */
    record ComandoCrear(CanalIngreso canal, Complejidad complejidad, Urgencia urgencia,
                        String idUnidadDestinoAlt, String resumen, String idRequirenteAlt,
                        Boolean esBetaOverride) {
    }

    /**
     * Comando de edición (partial-merge): cada campo nulo significa "no tocar".
     *
     * @param canal              nuevo canal (nulable)
     * @param complejidad        nueva complejidad (nulable)
     * @param urgencia           nueva urgencia (nulable)
     * @param idUnidadDestinoAlt nueva unidad de destino (nulable)
     * @param resumen            nuevo resumen (nulable)
     * @param idRequirenteAlt    nuevo requirente (nulable)
     */
    record ComandoEditar(CanalIngreso canal, Complejidad complejidad, Urgencia urgencia,
                         String idUnidadDestinoAlt, String resumen, String idRequirenteAlt) {
    }

    /**
     * Derecho vulnerado solicitado para un NNA.
     *
     * @param idDerechoAlt alt_key del derecho en el Catálogo (obligatorio)
     * @param idCausaAlt   alt_key de la causa en el Catálogo (opcional)
     */
    record DerechoSolicitado(String idDerechoAlt, String idCausaAlt) {
    }

    /**
     * Comando de alta de un NNA afectado con sus derechos.
     *
     * @param idPersonaAlt alt_key del NNA en personas-service (obligatorio)
     * @param derechos     derechos vulnerados a imputar (puede ser vacío)
     */
    record ComandoAgregarNna(String idPersonaAlt, List<DerechoSolicitado> derechos) {
        public ComandoAgregarNna {
            derechos = (derechos == null) ? List.of() : List.copyOf(derechos);
        }
    }

    /**
     * Comando de decisión de admisibilidad (USR.02): exactamente una de tres acciones disjuntas.
     *
     * @param accion                   acción a ejecutar
     * @param escaladoADefensora       solo aplica a INADMISIBLE
     * @param idProfesionalAsignadoAlt solo aplica (y es obligatorio) en ASIGNACION
     * @param observacion              texto libre opcional
     */
    record ComandoAdmisibilidad(AccionAdmisibilidad accion, boolean escaladoADefensora,
                                String idProfesionalAsignadoAlt, String observacion) {
    }

    /**
     * Criterios de listado de requerimientos.
     *
     * @param estado             filtro opcional por estado (nulo = todos)
     * @param idUnidadDestinoAlt filtro opcional por unidad de destino (nulo = todas)
     * @param pagina             índice de página (base 0)
     * @param tamano             tamaño de página
     */
    record FiltroListado(EstadoRequerimiento estado, String idUnidadDestinoAlt, int pagina, int tamano) {
    }
}

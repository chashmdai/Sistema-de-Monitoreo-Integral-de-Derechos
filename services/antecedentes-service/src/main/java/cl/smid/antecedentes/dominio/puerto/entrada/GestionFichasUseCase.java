package cl.smid.antecedentes.dominio.puerto.entrada;

import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;

import java.util.List;
import java.util.Set;

/**
 * Caso de uso de gestion de fichas de antecedentes (territoriales). Los identificadores de
 * referencia en los comandos ({@code procesoId}, {@code categoriaPrincipalId},
 * {@code categoriasSecundariasIds}, {@code instrumentoId}) transportan alt_key (UUID); la PK
 * interna nunca se expone (override #2).
 */
public interface GestionFichasUseCase {

    /** Crea una ficha en BORRADOR con el territorio y la autoria tomados del contexto. */
    FichaAntecedente crear(ComandoCrear comando, ContextoSesion contexto);

    /** Obtiene el detalle de una ficha (con relato en claro) dentro del alcance; 404 si no. */
    FichaAntecedente obtener(String altKey, ContextoSesion contexto);

    /** Edita una ficha en BORRADOR de la misma unidad; 404 si fuera de alcance. */
    FichaAntecedente editar(String altKey, ComandoEditar comando, ContextoSesion contexto);

    /** Elimina una ficha en BORRADOR de la misma unidad; 404 si fuera de alcance. */
    void eliminar(String altKey, ContextoSesion contexto);

    /** Aplica una transicion de revision (enviar/devolver/aprobar/rechazar). */
    FichaAntecedente transicionar(String altKey, ComandoAccionRevision comando, ContextoSesion contexto);

    /** Lista resumenes de ficha acotados al alcance y a los filtros funcionales. */
    Pagina<ResumenFicha> listar(FiltroFichas filtro, ContextoSesion contexto);

    /** Agrega el metadato de un documento a la ficha. */
    FichaAntecedente agregarDocumento(String altKey, ComandoAgregarDocumento comando, ContextoSesion contexto);

    // -----------------------------------------------------------------------------------
    // Comandos
    // -----------------------------------------------------------------------------------

    /**
     * Comando de creacion de ficha.
     *
     * @param procesoId                alt_key del proceso DDN (obligatorio, vigente)
     * @param casoAlt                  alt_key del caso 6.4 (opcional)
     * @param categoriaPrincipalId     alt_key de la categoria principal (obligatoria, vigente)
     * @param categoriasSecundariasIds alt_key de categorias secundarias (max 2, vigentes)
     * @param derechosCdn              articulos CDN 1..54 sin duplicados
     * @param descripcion              descripcion (obligatoria)
     * @param relato                   relato sensible (obligatorio; se cifra en reposo)
     * @param calificacion             calificacion (obligatoria)
     * @param criterios                criterios de relevancia
     * @param percepcionHallazgo       evaluacion de hallazgo
     * @param jefaturaAlt              alt_key de jefatura (opcional)
     * @param hallazgoAlt              alt_key de hallazgo existente (solo ANTECEDENTE_DE_HALLAZGO)
     * @param propuestaHallazgo        datos del hallazgo a proponer (solo SE_PROPONE_HALLAZGO)
     */
    record ComandoCrear(
            String procesoId,
            String casoAlt,
            String categoriaPrincipalId,
            List<String> categoriasSecundariasIds,
            List<Integer> derechosCdn,
            String descripcion,
            String relato,
            Calificacion calificacion,
            Set<Criterio> criterios,
            PercepcionHallazgo percepcionHallazgo,
            String jefaturaAlt,
            String hallazgoAlt,
            DatosPropuestaHallazgo propuestaHallazgo) {
    }

    /**
     * Comando de edicion de ficha (mismos campos de contenido que la creacion).
     */
    record ComandoEditar(
            String procesoId,
            String casoAlt,
            String categoriaPrincipalId,
            List<String> categoriasSecundariasIds,
            List<Integer> derechosCdn,
            String descripcion,
            String relato,
            Calificacion calificacion,
            Set<Criterio> criterios,
            PercepcionHallazgo percepcionHallazgo,
            String jefaturaAlt,
            String hallazgoAlt,
            DatosPropuestaHallazgo propuestaHallazgo) {
    }

    /**
     * Datos del hallazgo propuesto cuando la percepcion es {@code SE_PROPONE_HALLAZGO}.
     *
     * @param titulo                titulo del hallazgo
     * @param descripcion           descripcion del hallazgo
     * @param instrumentoId         alt_key del instrumento (obligatorio, vigente)
     * @param temporalidad          horizonte temporal
     * @param unidadesInvolucradas  alt_key de unidades involucradas
     * @param institucionesExternas alt_key de instituciones externas (6.10)
     */
    record DatosPropuestaHallazgo(
            String titulo,
            String descripcion,
            String instrumentoId,
            Temporalidad temporalidad,
            List<String> unidadesInvolucradas,
            List<String> institucionesExternas) {
    }

    /** Comando de transicion de revision. */
    record ComandoAccionRevision(AccionRevision accion, String observacion) {
    }

    /** Comando para agregar el metadato de un documento. */
    record ComandoAgregarDocumento(String nombre, String referenciaExterna) {
    }
}

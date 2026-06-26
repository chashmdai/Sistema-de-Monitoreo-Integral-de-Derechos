package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.excepcion.AccesoDenegadoException;
import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import cl.smid.antecedentes.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.EventoDominio;
import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Folio;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.PoliticaRoles;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioHallazgos;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;

import java.time.Instant;
import java.util.Map;

/**
 * Orquestador del caso de uso {@link GestionHallazgosUseCase}. Lectura nacional autenticada;
 * la creacion/edicion y el cambio de estado exigen rol de gestion/revisor. POJO de dominio,
 * cableado en {@code config.CableadoDominio}; sin transacciones (frontera en el controlador).
 */
public class ServicioHallazgos implements GestionHallazgosUseCase {

    private final RepositorioHallazgos repositorioHallazgos;
    private final RepositorioReferencias repositorioReferencias;
    private final GeneradorFolio generadorFolio;
    private final RelojDominio reloj;
    private final GeneradorIdentificadores generadorId;
    private final PublicadorEventos publicador;
    private final PoliticaRoles politicaRoles;

    public ServicioHallazgos(RepositorioHallazgos repositorioHallazgos, RepositorioReferencias repositorioReferencias,
                             GeneradorFolio generadorFolio, RelojDominio reloj, GeneradorIdentificadores generadorId,
                             PublicadorEventos publicador, PoliticaRoles politicaRoles) {
        this.repositorioHallazgos = repositorioHallazgos;
        this.repositorioReferencias = repositorioReferencias;
        this.generadorFolio = generadorFolio;
        this.reloj = reloj;
        this.generadorId = generadorId;
        this.publicador = publicador;
        this.politicaRoles = politicaRoles;
    }

    @Override
    public Hallazgo crear(ComandoCrear comando, ContextoSesion contexto) {
        exigirRolGestion(contexto);
        Instant ahora = reloj.ahora();
        validarInstrumento(comando.instrumentoId());

        Folio folio = generadorFolio.siguienteHallazgo();
        Hallazgo hallazgo = Hallazgo.proponer(generadorId.nuevoAltKey(), folio.valor(), comando.titulo(),
                comando.descripcion(), comando.instrumentoId(), comando.temporalidad(),
                comando.unidadesInvolucradas(), comando.institucionesExternas(), null, ahora);

        Hallazgo persistido = repositorioHallazgos.guardar(hallazgo);
        publicador.publicar(new EventoDominio("hallazgo.creado", persistido.altKey(), ahora,
                Map.of("temporalidad", persistido.temporalidad().name())));
        return persistido;
    }

    @Override
    public Hallazgo obtener(String altKey, ContextoSesion contexto) {
        return cargar(altKey);
    }

    @Override
    public Hallazgo editar(String altKey, ComandoEditar comando, ContextoSesion contexto) {
        exigirRolGestion(contexto);
        Instant ahora = reloj.ahora();
        Hallazgo hallazgo = cargar(altKey);
        validarInstrumento(comando.instrumentoId());
        hallazgo.editar(comando.titulo(), comando.descripcion(), comando.instrumentoId(), comando.temporalidad(),
                comando.unidadesInvolucradas(), comando.institucionesExternas(), ahora);
        return repositorioHallazgos.guardar(hallazgo);
    }

    @Override
    public Hallazgo cambiarEstado(String altKey, ComandoCambiarEstado comando, ContextoSesion contexto) {
        exigirRolGestion(contexto);
        Instant ahora = reloj.ahora();
        if (comando.estado() == null) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "El estado destino es obligatorio.");
        }
        Hallazgo hallazgo = cargar(altKey);
        hallazgo.cambiarEstado(comando.estado(), ahora);
        Hallazgo persistido = repositorioHallazgos.guardar(hallazgo);
        publicador.publicar(new EventoDominio("hallazgo.estado", persistido.altKey(), ahora,
                Map.of("estado", persistido.estado().name())));
        return persistido;
    }

    @Override
    public Pagina<Hallazgo> listar(FiltroHallazgos filtro, ContextoSesion contexto) {
        return repositorioHallazgos.buscar(filtro);
    }

    // ---------------------------------------------------------------------------------------

    private Hallazgo cargar(String altKey) {
        return repositorioHallazgos.buscarPorAltKey(altKey)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un hallazgo con identificador " + altKey + "."));
    }

    private void validarInstrumento(String instrumentoId) {
        if (instrumentoId == null || instrumentoId.isBlank()
                || !repositorioReferencias.existeVigentePorAltKey(TipoReferencia.INSTRUMENTO, instrumentoId)) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "El instrumento indicado no existe o no esta vigente.");
        }
    }

    private void exigirRolGestion(ContextoSesion contexto) {
        if (!contexto.tieneAlgunRol(politicaRoles.rolesRevision())) {
            throw new AccesoDenegadoException(
                    "La gestion de hallazgos exige un rol de gestion/revisor.");
        }
    }
}

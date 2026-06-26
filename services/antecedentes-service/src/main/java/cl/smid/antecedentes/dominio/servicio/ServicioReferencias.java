package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.excepcion.AccesoDenegadoException;
import cl.smid.antecedentes.dominio.excepcion.ConflictoUnicidadException;
import cl.smid.antecedentes.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.PoliticaRoles;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;

import java.time.Instant;

/**
 * Orquestador del caso de uso {@link GestionReferenciasUseCase} (categorias, procesos,
 * instrumentos). Lectura nacional autenticada; escritura por rol administrador. POJO de
 * dominio, cableado en {@code config.CableadoDominio}; sin transacciones.
 */
public class ServicioReferencias implements GestionReferenciasUseCase {

    private final RepositorioReferencias repositorio;
    private final RelojDominio reloj;
    private final GeneradorIdentificadores generadorId;
    private final PoliticaRoles politicaRoles;

    public ServicioReferencias(RepositorioReferencias repositorio, RelojDominio reloj,
                               GeneradorIdentificadores generadorId, PoliticaRoles politicaRoles) {
        this.repositorio = repositorio;
        this.reloj = reloj;
        this.generadorId = generadorId;
        this.politicaRoles = politicaRoles;
    }

    @Override
    public Referencia crear(TipoReferencia tipo, ComandoCrear comando, ContextoSesion contexto) {
        exigirRolAdmin(contexto);
        Instant ahora = reloj.ahora();
        if (comando.codigo() == null || comando.codigo().isBlank()) {
            throw new ConflictoUnicidadException("El codigo de la referencia es obligatorio.");
        }
        if (repositorio.existePorCodigo(tipo, comando.codigo().trim())) {
            throw new ConflictoUnicidadException(
                    "Ya existe una referencia de tipo " + tipo + " con el codigo '" + comando.codigo().trim() + "'.");
        }
        Referencia referencia = Referencia.crear(generadorId.nuevoAltKey(), tipo, comando.codigo(),
                comando.nombre(), ahora);
        return repositorio.guardar(referencia);
    }

    @Override
    public Referencia obtener(TipoReferencia tipo, String altKey, ContextoSesion contexto) {
        return cargar(tipo, altKey);
    }

    @Override
    public Referencia editar(TipoReferencia tipo, String altKey, ComandoEditar comando, ContextoSesion contexto) {
        exigirRolAdmin(contexto);
        Instant ahora = reloj.ahora();
        Referencia referencia = cargar(tipo, altKey);
        referencia.editar(comando.nombre(), ahora);
        return repositorio.guardar(referencia);
    }

    @Override
    public Referencia cambiarVigencia(TipoReferencia tipo, String altKey, ComandoVigencia comando,
                                      ContextoSesion contexto) {
        exigirRolAdmin(contexto);
        Instant ahora = reloj.ahora();
        Referencia referencia = cargar(tipo, altKey);
        referencia.cambiarVigencia(comando.vigente(), ahora);
        return repositorio.guardar(referencia);
    }

    @Override
    public Pagina<Referencia> listar(TipoReferencia tipo, String texto, Boolean vigente, int pagina, int tamano,
                                     ContextoSesion contexto) {
        return repositorio.listar(tipo, texto, vigente, pagina, tamano);
    }

    // ---------------------------------------------------------------------------------------

    private Referencia cargar(TipoReferencia tipo, String altKey) {
        return repositorio.buscarPorAltKey(tipo, altKey)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una referencia de tipo " + tipo + " con identificador " + altKey + "."));
    }

    private void exigirRolAdmin(ContextoSesion contexto) {
        if (!contexto.tieneAlgunRol(politicaRoles.rolesAdmin())) {
            throw new AccesoDenegadoException(
                    "La escritura de tablas de referencia exige un rol administrador.");
        }
    }
}

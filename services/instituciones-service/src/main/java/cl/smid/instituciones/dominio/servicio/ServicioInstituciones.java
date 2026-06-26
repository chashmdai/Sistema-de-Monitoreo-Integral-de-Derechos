package cl.smid.instituciones.dominio.servicio;

import cl.smid.instituciones.dominio.excepcion.ConflictoException;
import cl.smid.instituciones.dominio.excepcion.NoAutorizadoException;
import cl.smid.instituciones.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.instituciones.dominio.excepcion.ReglaNegocioException;
import cl.smid.instituciones.dominio.modelo.Ambito;
import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.DetalleInstitucion;
import cl.smid.instituciones.dominio.modelo.EventoDominio;
import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.GestionInstitucionesUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.GestionPuntosFocalesUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.GestionTiposUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosPuntoFocal;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosTipo;
import cl.smid.instituciones.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.instituciones.dominio.puerto.salida.PublicadorEventos;
import cl.smid.instituciones.dominio.puerto.salida.RelojDominio;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioInstituciones;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioPuntosFocales;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioTipos;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Orquestador único del dominio de instituciones: implementa los tres puertos de
 * entrada (tipos, instituciones y puntos focales) reutilizando los mismos puertos de
 * salida y reglas transversales.
 *
 * <p>Es un POJO puro (sin Spring/JPA): se construye y cablea en
 * {@code config.CableadoDominio}. No define límites transaccionales; el
 * {@code @Transactional} se aplica en la frontera (controladores y, si los hubiera,
 * listeners), de acuerdo con el override #7.</p>
 *
 * <p>Autorización (override #6): las <strong>lecturas</strong> no requieren contexto;
 * las <strong>escrituras</strong> exigen que la sesión posea alguno de los roles
 * administradores inyectados; en caso contrario se lanza {@link NoAutorizadoException}
 * ({@code AUTZ-004 / 403}).</p>
 */
public class ServicioInstituciones
        implements GestionTiposUseCase, GestionInstitucionesUseCase, GestionPuntosFocalesUseCase {

    private final RepositorioTipos repositorioTipos;
    private final RepositorioInstituciones repositorioInstituciones;
    private final RepositorioPuntosFocales repositorioPuntosFocales;
    private final PublicadorEventos publicadorEventos;
    private final RelojDominio reloj;
    private final GeneradorIdentificadores generador;
    private final Set<String> rolesAdmin;

    public ServicioInstituciones(RepositorioTipos repositorioTipos,
                                 RepositorioInstituciones repositorioInstituciones,
                                 RepositorioPuntosFocales repositorioPuntosFocales,
                                 PublicadorEventos publicadorEventos,
                                 RelojDominio reloj,
                                 GeneradorIdentificadores generador,
                                 Set<String> rolesAdmin) {
        this.repositorioTipos = repositorioTipos;
        this.repositorioInstituciones = repositorioInstituciones;
        this.repositorioPuntosFocales = repositorioPuntosFocales;
        this.publicadorEventos = publicadorEventos;
        this.reloj = reloj;
        this.generador = generador;
        this.rolesAdmin = rolesAdmin == null ? Set.of() : Set.copyOf(rolesAdmin);
    }

    // ===================== Tipos de institución =====================

    @Override
    public TipoInstitucion crearTipo(ContextoSesion contexto, DatosTipo datos) {
        exigirAdmin(contexto);
        Ambito ambito = Ambito.desde(datos.ambito());
        Instant ahora = reloj.ahora();
        TipoInstitucion tipo = TipoInstitucion.crear(
                generador.nuevoAltKey(), datos.nombre(), ambito, datos.descripcion(), ahora);
        if (repositorioTipos.existePorNombre(tipo.nombre())) {
            throw new ConflictoException(
                    "Ya existe un tipo de institución con el nombre '" + tipo.nombre() + "'.");
        }
        TipoInstitucion guardado = repositorioTipos.guardar(tipo);
        publicar("tipo.creado", guardado.altKey(),
                Map.of("nombre", guardado.nombre(), "ambito", guardado.ambito().name()), ahora);
        return guardado;
    }

    @Override
    public TipoInstitucion editarTipo(ContextoSesion contexto, String altKey, DatosTipo datos) {
        exigirAdmin(contexto);
        TipoInstitucion tipo = repositorioTipos.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoTipo(altKey));
        Ambito ambito = Ambito.desde(datos.ambito());
        tipo.actualizar(datos.nombre(), ambito, datos.descripcion(), reloj.ahora());
        if (repositorioTipos.existePorNombreExcluyendo(tipo.nombre(), altKey)) {
            throw new ConflictoException(
                    "Ya existe otro tipo de institución con el nombre '" + tipo.nombre() + "'.");
        }
        return repositorioTipos.guardar(tipo);
    }

    @Override
    public TipoInstitucion cambiarVigenciaTipo(ContextoSesion contexto, String altKey, boolean vigente) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        TipoInstitucion tipo = repositorioTipos.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoTipo(altKey));
        tipo.establecerVigencia(vigente, ahora);
        TipoInstitucion guardado = repositorioTipos.guardar(tipo);
        publicar("tipo.activacion", guardado.altKey(), Map.of("vigente", guardado.vigente()), ahora);
        return guardado;
    }

    @Override
    public TipoInstitucion obtenerTipo(String altKey) {
        return repositorioTipos.buscarPorAlt(altKey).orElseThrow(() -> noEncontradoTipo(altKey));
    }

    @Override
    public Pagina<TipoInstitucion> listarTipos(FiltroTipos filtro, Paginado paginado) {
        return repositorioTipos.listar(filtro, paginado);
    }

    // ===================== Instituciones =====================

    @Override
    public DetalleInstitucion crearInstitucion(ContextoSesion contexto, DatosInstitucion datos) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        Institucion institucion = Institucion.crear(
                generador.nuevoAltKey(),
                datos.codigo(), datos.nombre(), datos.tipoAlt(), datos.rut(),
                datos.regionCodigo(), datos.comunaCodigo(), datos.direccion(),
                datos.telefono(), datos.email(), datos.sitioWeb(), ahora);
        TipoInstitucion tipo = exigirTipoVigente(institucion.tipoAlt());
        verificarCodigoUnico(institucion.codigo(), null);
        Institucion guardada = repositorioInstituciones.guardar(institucion);
        publicar("institucion.creada", guardada.altKey(),
                Map.of("nombre", guardada.nombre(), "tipoAlt", guardada.tipoAlt(),
                        "ambito", tipo.ambito().name()), ahora);
        return new DetalleInstitucion(guardada, tipo.nombre(), tipo.ambito(), List.of());
    }

    @Override
    public DetalleInstitucion editarInstitucion(ContextoSesion contexto, String altKey, DatosInstitucion datos) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        Institucion institucion = repositorioInstituciones.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoInstitucion(altKey));
        institucion.actualizar(
                datos.codigo(), datos.nombre(), datos.tipoAlt(), datos.rut(),
                datos.regionCodigo(), datos.comunaCodigo(), datos.direccion(),
                datos.telefono(), datos.email(), datos.sitioWeb(), ahora);
        TipoInstitucion tipo = exigirTipoVigente(institucion.tipoAlt());
        verificarCodigoUnico(institucion.codigo(), altKey);
        Institucion guardada = repositorioInstituciones.guardar(institucion);
        publicar("institucion.actualizada", guardada.altKey(), Map.of(), ahora);
        List<PuntoFocal> puntos = repositorioPuntosFocales.listarPorInstitucion(altKey);
        return new DetalleInstitucion(guardada, tipo.nombre(), tipo.ambito(), puntos);
    }

    @Override
    public DetalleInstitucion cambiarActivacionInstitucion(ContextoSesion contexto, String altKey, boolean activa) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        Institucion institucion = repositorioInstituciones.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoInstitucion(altKey));
        institucion.establecerActiva(activa, ahora);
        Institucion guardada = repositorioInstituciones.guardar(institucion);
        publicar("institucion.activacion", guardada.altKey(), Map.of("activa", guardada.activa()), ahora);
        TipoInstitucion tipo = resolverTipo(guardada.tipoAlt());
        List<PuntoFocal> puntos = repositorioPuntosFocales.listarPorInstitucion(altKey);
        return new DetalleInstitucion(guardada, tipo.nombre(), tipo.ambito(), puntos);
    }

    @Override
    public DetalleInstitucion obtenerInstitucion(String altKey) {
        Institucion institucion = repositorioInstituciones.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoInstitucion(altKey));
        TipoInstitucion tipo = resolverTipo(institucion.tipoAlt());
        List<PuntoFocal> puntos = repositorioPuntosFocales.listarPorInstitucion(altKey);
        return new DetalleInstitucion(institucion, tipo.nombre(), tipo.ambito(), puntos);
    }

    @Override
    public Pagina<ResumenInstitucion> listarInstituciones(FiltroInstituciones filtro, Paginado paginado) {
        return repositorioInstituciones.listar(filtro, paginado);
    }

    // ===================== Puntos focales =====================

    @Override
    public PuntoFocal crearPuntoFocal(ContextoSesion contexto, String institucionAlt, DatosPuntoFocal datos) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        // La institución debe existir (la invariante de unicidad de principal es por institución).
        repositorioInstituciones.buscarPorAlt(institucionAlt)
                .orElseThrow(() -> noEncontradoInstitucion(institucionAlt));
        PuntoFocal puntoFocal = PuntoFocal.crear(
                generador.nuevoAltKey(), institucionAlt, datos.nombre(), datos.cargo(),
                datos.email(), datos.telefono(), datos.principal(), ahora);
        PuntoFocal guardado = repositorioPuntosFocales.guardar(puntoFocal);
        sostenerPrincipalUnico(guardado, ahora);
        publicar("puntofocal.creado", guardado.altKey(), Map.of("institucionAlt", institucionAlt), ahora);
        return guardado;
    }

    @Override
    public PuntoFocal editarPuntoFocal(ContextoSesion contexto, String altKey, DatosPuntoFocal datos) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        PuntoFocal puntoFocal = repositorioPuntosFocales.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoPuntoFocal(altKey));
        puntoFocal.actualizar(datos.nombre(), datos.cargo(), datos.email(), datos.telefono(),
                datos.principal(), ahora);
        PuntoFocal guardado = repositorioPuntosFocales.guardar(puntoFocal);
        sostenerPrincipalUnico(guardado, ahora);
        return guardado;
    }

    @Override
    public PuntoFocal cambiarActivacionPuntoFocal(ContextoSesion contexto, String altKey, boolean activo) {
        exigirAdmin(contexto);
        Instant ahora = reloj.ahora();
        PuntoFocal puntoFocal = repositorioPuntosFocales.buscarPorAlt(altKey)
                .orElseThrow(() -> noEncontradoPuntoFocal(altKey));
        puntoFocal.establecerActivo(activo, ahora);
        PuntoFocal guardado = repositorioPuntosFocales.guardar(puntoFocal);
        sostenerPrincipalUnico(guardado, ahora);
        publicar("puntofocal.activacion", guardado.altKey(), Map.of("activo", guardado.activo()), ahora);
        return guardado;
    }

    // ===================== Apoyos privados =====================

    /**
     * Garantiza que, si el punto focal quedó como principal y activo, ningún otro punto
     * focal activo de la misma institución conserve la condición de principal.
     */
    private void sostenerPrincipalUnico(PuntoFocal puntoFocal, Instant ahora) {
        if (puntoFocal.principal() && puntoFocal.activo()) {
            repositorioPuntosFocales.desmarcarOtrosPrincipales(
                    puntoFocal.institucionAlt(), puntoFocal.altKey());
        }
    }

    private TipoInstitucion exigirTipoVigente(String tipoAlt) {
        TipoInstitucion tipo = repositorioTipos.buscarPorAlt(tipoAlt)
                .orElseThrow(() -> new ReglaNegocioException(
                        "El tipo de institución '" + tipoAlt + "' no existe."));
        if (!tipo.vigente()) {
            throw new ReglaNegocioException(
                    "El tipo de institución '" + tipoAlt + "' no está vigente y no puede asociarse.");
        }
        return tipo;
    }

    private TipoInstitucion resolverTipo(String tipoAlt) {
        return repositorioTipos.buscarPorAlt(tipoAlt)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El tipo de institución asociado no existe."));
    }

    private void verificarCodigoUnico(String codigo, String altKeyExcluido) {
        if (codigo == null) {
            return;
        }
        boolean existe = altKeyExcluido == null
                ? repositorioInstituciones.existePorCodigo(codigo)
                : repositorioInstituciones.existePorCodigoExcluyendo(codigo, altKeyExcluido);
        if (existe) {
            throw new ConflictoException("Ya existe una institución con el código '" + codigo + "'.");
        }
    }

    private void exigirAdmin(ContextoSesion contexto) {
        if (contexto == null || !contexto.poseeAlgunRol(rolesAdmin)) {
            throw new NoAutorizadoException(
                    "La operación requiere un rol administrador sobre instituciones.");
        }
    }

    private void publicar(String tipo, String altKey, Map<String, Object> metadatos, Instant ahora) {
        publicadorEventos.publicar(EventoDominio.de(tipo, altKey, metadatos, ahora));
    }

    private static RecursoNoEncontradoException noEncontradoTipo(String altKey) {
        return new RecursoNoEncontradoException("No existe un tipo de institución con alt_key '" + altKey + "'.");
    }

    private static RecursoNoEncontradoException noEncontradoInstitucion(String altKey) {
        return new RecursoNoEncontradoException("No existe una institución con alt_key '" + altKey + "'.");
    }

    private static RecursoNoEncontradoException noEncontradoPuntoFocal(String altKey) {
        return new RecursoNoEncontradoException("No existe un punto focal con alt_key '" + altKey + "'.");
    }
}
